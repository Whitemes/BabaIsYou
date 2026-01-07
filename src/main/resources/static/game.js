const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const statusDiv = document.getElementById('status');
const BLOCK_SIZE = 24; // Adjust based on image size

let socket;
const images = {};
const IMAGE_NAMES = [
    'babaEntity', 'babaWord', 'brickEntity', 'brickWord', 'defeatWord', 'emptyEntity',
    'flagEntity', 'flagWord', 'flowerEntity', 'flowerWord', 'grassEntity', 'grassWord',
    'hotWord', 'isWord', 'lavaEntity', 'lavaWord', 'meltWord', 'pushWord',
    'rockEntity', 'rockWord', 'sinkWord', 'skullEntity', 'skullWord', 'smileyEntity', 'smileyWord',
    'stopWord', 'jumpWord', 'tileEntity', 'tileWord', 'wallEntity', 'wallWord',
    'waterEntity', 'waterWord', 'winWord', 'youWord'
];

// Load Images
let imagesLoaded = 0;
let imageErrors = 0;

console.log('Loading', IMAGE_NAMES.length, 'images...');
statusDiv.innerText = `Loading assets... (0/${IMAGE_NAMES.length})`;

IMAGE_NAMES.forEach(name => {
    const img = new Image();
    img.src = `images/${name}.gif`;

    img.onload = () => {
        imagesLoaded++;
        statusDiv.innerText = `Loading assets... (${imagesLoaded}/${IMAGE_NAMES.length})`;

        if (imagesLoaded + imageErrors === IMAGE_NAMES.length) {
            if (imageErrors > 0) {
                console.warn(`Loaded ${imagesLoaded} images with ${imageErrors} errors`);
            } else {
                console.log('All images loaded successfully');
            }
            connect();
        }
    };

    img.onerror = () => {
        imageErrors++;
        console.error('Failed to load image:', name);
        statusDiv.innerText = `Loading assets... (${imagesLoaded}/${IMAGE_NAMES.length}, ${imageErrors} errors)`;

        if (imagesLoaded + imageErrors === IMAGE_NAMES.length) {
            console.warn(`Loaded ${imagesLoaded} images with ${imageErrors} errors`);
            connect();
        }
    };

    images[name] = img;
});

function connect() {
    // Dynamically choose WS or WSS based on current page protocol
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/game-ws`;

    console.log('Connecting to WebSocket:', wsUrl);
    statusDiv.innerText = 'Connecting to server...';

    socket = new WebSocket(wsUrl);

    socket.onopen = () => {
        console.log('WebSocket connection established');
        statusDiv.innerText = 'Connected. Use Arrow Keys to Move. R to Restart. Z to Undo.';
        statusDiv.style.color = '#4ade80'; // Green
    };

    socket.onmessage = (event) => {
        console.log('Received message from server');
        try {
            const data = JSON.parse(event.data);

            // Check if this is an error message
            if (data.error) {
                statusDiv.innerText = `Server Error: ${data.error}`;
                statusDiv.style.color = '#ef4444'; // Red
                console.error('Server error:', data.error);
                return;
            }

            // Normal grid data
            render(data);
        } catch (e) {
            console.error('Failed to parse server message:', e);
            statusDiv.innerText = 'Error: Invalid data received from server';
            statusDiv.style.color = '#ef4444'; // Red
        }
    };

    socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        statusDiv.innerText = 'Connection error. Check console for details.';
        statusDiv.style.color = '#ef4444'; // Red
    };

    socket.onclose = (event) => {
        console.log('WebSocket connection closed:', event.code, event.reason);
        statusDiv.innerText = `Disconnected (Code: ${event.code}). Refresh to reconnect.`;
        statusDiv.style.color = '#f59e0b'; // Orange
    };
}

function render(grid) {
    if (!grid || grid.length === 0) {
        console.warn('Cannot render: Grid is empty or undefined');
        statusDiv.innerText = 'Error: No game data received';
        statusDiv.style.color = '#ef4444'; // Red
        return;
    }

    console.log('Rendering grid:', grid.length, 'x', grid[0].length);

    const rows = grid.length;
    const cols = grid[0].length;

    canvas.width = cols * BLOCK_SIZE;
    canvas.height = rows * BLOCK_SIZE;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    let renderedElements = 0;

    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const cell = grid[r][c];
            // Cell is a list of elements? Or simplified?
            // "Cellule" object has "elements" field.
            // JSON structure: { elements: ["BABA", "IS", "YOU"] } (Enums as strings)

            if (cell.elements) {
                cell.elements.forEach(el => {
                    const imgName = getElementName(el);
                    if (images[imgName]) {
                        ctx.drawImage(images[imgName], c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        renderedElements++;
                    } else if (el !== 'EMPTY') {
                        console.warn('Missing image for element:', el, '(expected:', imgName + ')');
                        // Fallback: draw colored square
                        ctx.fillStyle = '#ff00ff';
                        ctx.fillRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        ctx.fillStyle = 'white';
                        ctx.font = '8px monospace';
                        ctx.fillText(el.substring(0, 4), c * BLOCK_SIZE + 2, r * BLOCK_SIZE + 12);
                    }
                });
            }
        }
    }

    console.log('Rendered', renderedElements, 'elements');
}

function getElementName(enumStr) {
    // Map Enum String (e.g., "ENTITY_BABA") to Image Name ("babaEntity")
    // Rules: 
    // "ENTITY_X" -> "xEntity"
    // "X" -> "xWord"

    if (enumStr.startsWith('ENTITY_')) {
        let core = enumStr.substring(7).toLowerCase();
        return core + 'Entity';
    } else {
        return enumStr.toLowerCase() + 'Word';
    }
}

// Input Handling
window.addEventListener('keydown', (e) => {
    if (!socket || socket.readyState !== WebSocket.OPEN) return;

    let command = null;
    switch (e.key) {
        case 'ArrowUp': command = 'UP'; break;
        case 'ArrowDown': command = 'DOWN'; break;
        case 'ArrowLeft': command = 'LEFT'; break;
        case 'ArrowRight': command = 'RIGHT'; break;
        case 'r': command = 'RESTART'; break;
        case 'z': command = 'UNDO'; break;
    }

    if (command) {
        socket.send(command);
    }
});
