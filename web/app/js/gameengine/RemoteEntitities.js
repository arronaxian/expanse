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
            if ( noise.load(this.x, this.y) == 2 ) {
                for (var x = 0; x < this.scene.chunkSize; x++) {
                    for (var y = 0; y < this.scene.chunkSize; y++) {
                        var perlinValue = noise.perlin2(this.x,this.y,x,y) / 10;

                        var key = "";
                        var animationKey = "";

                        let type = 0;
                        if (perlinValue < 0.3) {
                            key = "sprMountain";
                            type = 2;
                        } else if ( perlinValue >= 0.3 && perlinValue < 0.4 ) {
                            key = "sprTree";
                            type = 0;
                        }
                        else if (perlinValue >= 0.4 && perlinValue < 1.1 ) {
                            key = "sprGrass";
                            type = 0;
                        }
                        else if (perlinValue >= 1.1 && perlinValue < 1.3) {
                            key = "sprTree";
                            type = 0;
                        }
                        else if (perlinValue >= 1.3) {
                            key = "sprWater";
                            animationKey = "sprWater";
                            type = 2;
                        }
                        let tileX = (this.x * (this.scene.chunkSize * this.scene.tileSize)) + (x * this.scene.tileSize);
                        let tileY = (this.y * (this.scene.chunkSize * this.scene.tileSize)) + (y * this.scene.tileSize);
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

        this.moveSize = 16;
        this.init();
        this.scene = scene;
        this.type = type;
        this.setDepth(1);
        this.setScale(0.25);
        this.scene.add.existing(this);
        this.setOrigin(0);

        this.moveHeading = 'none';
    }

    moveNorth() {
        if ( this.scene.getAngularScopeDelegate().isMovingPlayer() ) {
            return;
        }

        if ( this.isMovePending() ) {
            return;
        }

        if ( this.y <= 0 ) return;
        this.newY = this.y - this.moveSize;

        this.moveHeading = 'north';
    }

    moveSouth() {
        if ( this.scene.getAngularScopeDelegate().isMovingPlayer() ) {
            return;
        }

        if ( this.isMovePending() ) {
            return;
        }

        this.newY = this.y + this.moveSize;

        this.moveHeading = 'south';
    }

    moveEast() {
        if ( this.scene.getAngularScopeDelegate().isMovingPlayer() ) {
            return;
        }

        if ( this.isMovePending() ) {
            return;
        }

        this.newX = this.x + this.moveSize;

        this.moveHeading = 'east';
    }

    moveWest() {
        if ( this.scene.getAngularScopeDelegate().isMovingPlayer() ) {
            return;
        }

        if ( this.isMovePending() ) {
            return;
        }

        if ( this.x <= 0 ) return;
        this.newX = this.x - this.moveSize;

        this.moveHeading = 'west';
    }

    move() {
        let player = this;
        this.scene.getAngularScopeDelegate().movePlayer(this.moveHeading, function(data, status) {
            if ( status === 200 ) {
                player.x = data.x * player.moveSize;
                player.y = data.y * player.moveSize;
                player.newX = player.x;
                player.newY = player.y;
                player.moveHeading = '';
            }
        });
    }

    retreat() {
        this.newX = this.x;
        this.newY = this.y;
    }

    isMovePending() {
        return this.x != this.newX || this.y != this.newY;
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

    init() {
        this.x = this.scene.getAngularScopeDelegate().playContext.player.position.x * this.moveSize;
        this.y = this.scene.getAngularScopeDelegate().playContext.player.position.y * this.moveSize;
        this.newX = this.x;
        this.newY = this.y;
    }
}

class Dragon extends Player {
    constructor(scene, x, y, key, type) {
        super(scene, x, y, key, type);
    }

    canMove(terrain) {
        return true;
    }

    init() {
    }
}