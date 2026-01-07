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
IMAGE_NAMES.forEach(name => {
    const img = new Image();
    img.src = `images/${name}.gif`;
    img.onload = () => {
        imagesLoaded++;
        if (imagesLoaded === IMAGE_NAMES.length) {
            connect();
        }
    };
    images[name] = img;
});

function connect() {
    socket = new WebSocket(`ws://${window.location.host}/game-ws`);
    
    socket.onopen = () => {
        statusDiv.innerText = 'Connected. Use Arrow Keys to Move. R to Restart.';
    };

    socket.onmessage = (event) => {
        const grid = JSON.parse(event.data);
        render(grid);
    };

    socket.onclose = () => {
        statusDiv.innerText = 'Disconnected.';
    };
}

function render(grid) {
    if (!grid || grid.length === 0) return;
    
    const rows = grid.length;
    const cols = grid[0].length;
    
    canvas.width = cols * BLOCK_SIZE;
    canvas.height = rows * BLOCK_SIZE;
    
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
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
                    } else {
                         // Fallback text
                        // ctx.fillStyle = 'white';
                        // ctx.fillText(el, c * BLOCK_SIZE, r * BLOCK_SIZE + 12);
                    }
                });
            }
        }
    }
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
    switch(e.key) {
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
