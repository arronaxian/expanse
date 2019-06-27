class Chunk {
    constructor(scene, x, y) {
        this.scene = scene;
        this.x = x;
        this.y = y;
        this.type  = 0;
        this.tiles = this.scene.add.group();
        this.isLoaded = false;
    }

    unload() {
        if (this.isLoaded) {
            this.tiles.clear(true, true);
            this.isLoaded = false;
        }
    }

    load() {
        if (!this.isLoaded) {
            for (var x = 0; x < this.scene.chunkSize; x++) {
                for (var y = 0; y < this.scene.chunkSize; y++) {
                    var tileX = (this.x * (this.scene.chunkSize * this.scene.tileSize)) + (x * this.scene.tileSize);
                    var tileY = (this.y * (this.scene.chunkSize * this.scene.tileSize)) + (y * this.scene.tileSize);

                    var perlinValue = noise.perlin2(tileX / 100, tileY / 100);

                    var key = "";
                    var animationKey = "";

                    let type = 0;
                    if (perlinValue < 0) {
                        key = "sprGrass";
                        type = 0;
                    } else if (perlinValue >= 0.0 && perlinValue < 0.2) {
                        key = "sprSand";
                        type = 1;
                    } else if (perlinValue >= 0.2) {
                        key = "sprWater";
                        animationKey = "sprWater";
                        type = 2;
                    }

                    let tile = new Tile(this.scene, tileX, tileY, key, type);
                    if (animationKey !== "") {
                        tile.play(animationKey);
                    }
                    this.tiles.add(tile);
                }
            }

            this.isLoaded = true;
        }
    }
}

class Tile extends Phaser.GameObjects.Sprite {
    constructor(scene, x, y, key, type) {
        super(scene, x, y, key);
        this.scene = scene;
        this.type = type;
        this.scene.add.existing(this);
        this.setOrigin(0);
    }
}

class Player extends Phaser.GameObjects.Sprite {
    constructor(scene, x, y, key, type) {
        super(scene, x, y, key);
        this.newX = x;
        this.newY = y;
        this.scene = scene;
        this.type = type;
        this.setDepth(1);
        this.setScale(0.25);
        this.scene.add.existing(this);
        this.setOrigin(0);
        this.moveSize = 16;
    }

    moveNorth() {
        this.newY = this.y - this.moveSize;
    }

    moveSouth() {
        this.newY = this.y + this.moveSize;
    }

    moveEast() {
        this.newX = this.x + this.moveSize;
    }

    moveWest() {
        this.newX = this.x - this.moveSize;
    }

    move() {
        this.x = this.newX;
        this.y = this.newY;
    }

    retreat() {
        this.newX = this.x;
        this.newY = this.y;
    }

    isMovePending() {
        return  this.x != this.newX ||
                this.y != this.newY;
    }

    moveTowards(thing) {
        if (this.newX > thing.x ) {
            this.moveWest();
        }

        if (this.newX < thing.x ) {
            this.moveEast();
        }

        if ( this.newY > thing.y ) {
            this.moveNorth();
        }

        if ( this.newY < thing.y ) {
            this.moveSouth();
        }
    }

    canMove(terrain) {
        return terrain != null && terrain != 2;
    }
}

class Dragon extends Player {
    constructor(scene, x, y, key, type) {
        super(scene, x, y, key, type);
    }

    canMove(terrain) {
        return true;
    }
}