# BABA IS YOU

Implémentation Java du jeu de puzzle **BABA IS YOU**, développée dans le cadre d'un projet académique à l'ESIEE.

## Description

**BABA IS YOU** est un jeu de puzzle innovant où les règles du jeu font partie du niveau lui-même. Les joueurs manipulent des mots pour créer et modifier les règles, transformant ainsi la façon dont les éléments du jeu interagissent.

### Fonctionnalités principales

- **Système de règles dynamiques** : Les règles sont formées par des mots disposés dans le niveau (ex: "BABA IS YOU", "WALL IS STOP")
- **Transformations d'éléments** : Possibilité de transformer des objets en d'autres (ex: "ROCK IS WALL")
- **Propriétés multiples** : PUSH, STOP, WIN, DEFEAT, MELT, HOT, SINK, JUMP
- **Interface graphique** : Affichage avec images et animations
- **Multiples niveaux** : Support de niveaux personnalisés

## Prérequis

- **Java 21** ou supérieur
- **Apache Ant** (pour la compilation)
- Bibliothèque **ZEN 6.0** (incluse dans `lib/`)

## Structure du projet

```
RAMANANJATOVO_BabaIsYou/
├── src/                          # Code source Java
│   └── fr/esiee/baba/
│       ├── controller/           # Contrôleurs (Game, CommandLineParser)
│       ├── model/               # Modèle du jeu (Level, Rules, Elements...)
│       └── view/                # Interface graphique
├── resources/                   # Ressources du jeu
│   ├── images/                  # Images des éléments
│   └── text/                    # Fichiers de niveaux
├── lib/                         # Bibliothèques externes
├── docs/                        # Documentation
└── build.xml                    # Script de build Ant
```

## Installation et compilation

### Compilation avec Ant

```bash
# Compilation complète (nettoyage, compilation, JAR, documentation)
ant all

# Compilation seule
ant compile

# Création du JAR
ant jar

# Génération de la documentation
ant javadoc

# Nettoyage
ant clean
```

## Utilisation

### Lancement du jeu

```bash
# Niveau par défaut
java -jar dist/baba.jar

# Niveau spécifique
java -jar dist/baba.jar --level "path/to/level.txt"

# Tous les niveaux d'un dossier
java -jar dist/baba.jar --levels "path/to/levels/"

# Exécution de règles personnalisées
java -jar dist/baba.jar --execute "ROCK IS WIN"
```

### Contrôles

- **Flèches directionnelles** : Déplacement
- **Échap** : Quitter le jeu

### Format des niveaux

Les niveaux sont définis dans des fichiers texte avec des caractères spécifiques :

#### Entités
- `B` : Baba (entité)
- `F` : Drapeau (entité)
- `W` : Mur (entité)
- `R` : Rocher (entité)
- `A` : Eau (entité)
- `S` : Crâne (entité)
- `L` : Lave (entité)

#### Mots (règles)
- `b` : BABA (mot)
- `f` : FLAG (mot)
- `w` : WALL (mot)
- `r` : ROCK (mot)
- `i` : IS (opérateur)
- `y` : YOU (propriété)
- `v` : WIN (propriété)
- `t` : STOP (propriété)
- `p` : PUSH (propriété)

## Architecture

### Modèle MVC

Le projet suit le pattern **Modèle-Vue-Contrôleur** :

- **Modèle** : Gestion de l'état du jeu, règles, éléments
- **Vue** : Affichage graphique du jeu
- **Contrôleur** : Gestion des interactions utilisateur

### Classes principales

- `Game` : Contrôleur principal du jeu
- `Level` : Représentation d'un niveau
- `Rules` : Gestion des règles dynamiques
- `Transmutation` : Application des transformations
- `Element` : Énumération des éléments du jeu
- `View` : Interface graphique

## Exemples de règles

```
BABA IS YOU    # Baba est contrôlé par le joueur
WALL IS STOP   # Les murs bloquent le mouvement
FLAG IS WIN    # Toucher le drapeau fait gagner
ROCK IS PUSH   # Les rochers peuvent être poussés
LAVA IS HOT    # La lave est chaude
ICE IS MELT    # La glace peut fondre
```

## Tests

Des niveaux de test sont disponibles dans `resources/text/test/`.

## Documentation

La documentation Javadoc est générée dans `docs/doc/` avec la commande `ant javadoc`.

## Auteur

**RAMANANJATOVO** - Projet ESIEE Paris

## Licence

Projet académique - ESIEE Paris
