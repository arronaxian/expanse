class SceneMain extends Phaser.Scene {
    constructor() {
        super({ key: "SceneMain" });
    }

    preload() {
        this.load.spritesheet("sprWater", "./img/sprWater.png", {
            frameWidth: 16,
            frameHeight: 16
        });
        this.load.image("sprSand", "./img/sprSand.png");
        this.load.image("sprGrass", "./img/sprGrass.png");
        this.load.image("player", "./img/player.png");
        this.load.image("dragon", "./img/dragon.png");
    }

    create() {
        this.anims.create({
            key: "sprWater",
            frames: this.anims.generateFrameNumbers("sprWater"),
            frameRate: 5,
            repeat: -1
        });
        this.chunkSize = 16;
        this.tileSize = 16;
        this.cameras.main.setZoom(3);

        // player
        this.player1 = new Player(this,0,0,'player',1);
        this.dragons = [];
        this.dragons.push(new Dragon(this,(Math.round(Math.random()*30))*16, (Math.round(Math.random()*20))*16, 'dragon', 2));
        this.dragons.push(new Dragon(this,(Math.round(Math.random()*30))*16, (Math.round(Math.random()*20))*16, 'dragon', 2));
        this.dragons.push(new Dragon(this,(Math.round(Math.random()*30))*16, (Math.round(Math.random()*20))*16, 'dragon', 2));
        this.dragons.push(new Dragon(this,(Math.round(Math.random()*30))*16, (Math.round(Math.random()*20))*16, 'dragon', 2));

        this.chunks = [];
        this.keyW = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.W);
        this.keyS = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.S);
        this.keyA = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.A);
        this.keyD = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.D);
    }

    getChunk(x, y) {
        var chunk = null;
        for (var i = 0; i < this.chunks.length; i++) {
            if (this.chunks[i].x == x && this.chunks[i].y == y) {
                chunk = this.chunks[i];
            }
        }
        return chunk;
    }

    update() {
        var snappedChunkX = (this.chunkSize * this.tileSize) * Math.round(this.player1.x / (this.chunkSize * this.tileSize));
        var snappedChunkY = (this.chunkSize * this.tileSize) * Math.round(this.player1.y / (this.chunkSize * this.tileSize));
        snappedChunkX = snappedChunkX / this.chunkSize / this.tileSize;
        snappedChunkY = snappedChunkY / this.chunkSize / this.tileSize;

        for (var x = snappedChunkX - 2; x < snappedChunkX + 2; x++) {
            for (var y = snappedChunkY - 2; y < snappedChunkY + 2; y++) {
                var existingChunk = this.getChunk(x, y);
                if (existingChunk == null) {
                    var newChunk = new Chunk(this, x, y);
                    this.chunks.push(newChunk);
                }
            }
        }

        for (var i = 0; i < this.chunks.length; i++) {
            var chunk = this.chunks[i];
            if (Phaser.Math.Distance.Between(
                snappedChunkX,
                snappedChunkY,
                chunk.x,
                chunk.y
            ) < 3) {
                if (chunk !== null) {
                    chunk.load();
                }
            }
            else {
                if (chunk !== null) {
                    chunk.unload();
                }
            }
        }

        if (Phaser.Input.Keyboard.JustDown(this.keyW)) {
            this.player1.moveNorth();
        }
        if (Phaser.Input.Keyboard.JustDown(this.keyS)) {
            this.player1.moveSouth();
        }
        if (Phaser.Input.Keyboard.JustDown(this.keyA)) {
            this.player1.moveWest();
        }
        if (Phaser.Input.Keyboard.JustDown(this.keyD)) {
            this.player1.moveEast();
        }

        if ( this.player1.isMovePending() ) {
            let player = this.player1;
            for ( let i in this.dragons) {
                let dragon = this.dragons[i];
                if ( this.canMove(dragon) ) {
                    dragon.moveTowards(player);
                    dragon.move();
                }
            };
        }

        if ( this.player1.isMovePending() ) {
            this.canMove(this.player1) ? this.player1.move() : this.player1.retreat();
        }

        this.cameras.main.centerOn(this.player1.x, this.player1.y);
    }

    /**
     * Can the player move?
     * @param player The player.
     * @returns {*} True if
     */
    canMove(player) {
        let tile = this.getTile(player.newX, player.newY);
        return player.canMove(tile.type);
    }

    getTile(x,y) {
        let xChunk = Math.floor(x / 256);
        let yChunk = Math.floor(y / 256);
        let existingChunk = this.getChunk(xChunk, yChunk);
        let entries = existingChunk.tiles.children.entries;
        for ( let i = 0; i < entries.length; i++ ) {
            if ( entries[i].x === x && entries[i].y === y ) {
                return entries[i];
            }
        }
        return null;
    }
}