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
        let minPerlin = 99;
        let maxPerlin = -99;
        if (!this.isLoaded) {
            for (var x = 0; x < this.scene.chunkSize; x++) {
                for (var y = 0; y < this.scene.chunkSize; y++) {
                    var tileX = (this.x * (this.scene.chunkSize * this.scene.tileSize)) + (x * this.scene.tileSize);
                    var tileY = (this.y * (this.scene.chunkSize * this.scene.tileSize)) + (y * this.scene.tileSize);

                    var perlinValue = noise.perlin2(tileX / 100, tileY / 100);

                    var key = "";
                    var animationKey = "";

                    let type = 0;

                    minPerlin = Math.min(minPerlin, perlinValue);
                    maxPerlin = Math.max(maxPerlin, perlinValue);

                    if (perlinValue < -0.3) {
                        key = "sprSand";
                        type = 0;
                    } else if ( perlinValue >= -0.3 && perlinValue < 0.0 ) {
                            key = "sprTree";
                            type = 1;
                    } else if (perlinValue >= 0.0 && perlinValue < 0.4) {
                        key = "sprGrass";
                        type = 1;
                    } else if (perlinValue >= 0.4) {
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

            console.log(minPerlin,maxPerlin);

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
    heading = 'stop';

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
        this.heading = 'north';
    }

    moveSouth() {
        this.newY = this.y + this.moveSize;
        this.heading = 'south';
    }

    moveEast() {
        this.newX = this.x + this.moveSize;
        this.heading = 'east';
    }

    moveWest() {
        this.newX = this.x - this.moveSize;
        this.heading = 'west';
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
    targets = [];

    constructor(scene, x, y, key, type) {
        super(scene, x, y, key, type);

        this.start();
    }

    addTarget(player) {
        this.targets.push(player);
    }

    canMove(terrain) {
        return true;
    }

    hunt(me) {
        me.targets.forEach(function(target) {
            me.moveTowards(target);
            me.move();
        })
    }

    start() {
        var timer = this.scene.time.addEvent({
            delay: 5000,
            args: [this],
            callback: this.hunt,
            loop: true
        });
    }

    stop() {
    }

    processEvent(me) {
        me.targets.forEach(function(target) {
            me.moveTowards(target);
        })
    }
}