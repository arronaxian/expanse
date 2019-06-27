/*
 * A remote perlin and simplex noise algorithms for 2D.
 */

(function(global){
    let module = global.noise = {};

    let precomp = [];

    module.cache = function(areaX,areaY) {
        const areasize = 16;

        // Search for a cached area
        for ( let i = 0; i < precomp.length; i++ ) {
            if ( precomp[i].areaX == areaX && precomp[i].areaY == areaY ) {
                return precomp[i];
            }
        }

        // Not found so build the proxy object plus accessor and push it cache
        let currentArea = {
            areaX: areaX,
            areaY: areaY,
            status: 1, // loading
            area: null,
            perlin: function(x,y) {
                if ( this.status == 2 ) {
                    return currentArea.area[y][x];
                } else {
                    return 0;
                }
            }
        };
        console.log('caching ',areaX,areaY);
        precomp.push(currentArea);

        let auth = window.localStorage.getItem("com.ds.expanse.jwt");
        if ( auth ) {
            let x1 = ( areaX * areasize );
            let y1 = ( areaY * areasize );

            let params = {
                "x1":  x1,
                "y1":  y1,
                "x2": x1 + areasize,
                "y2": y1 + areasize
            };

            // make ajax call
            $.ajax({    url:'http://localhost:9092/cartograph/perlin/rect',
                        data:params,
                        type: 'GET',
                        headers:{'Authorization':auth},
            }).done(function(data) {
                let area = [];
                let index = 0;
                for ( let ay = 0; ay < areasize; ay ++ ) {
                    let row = [];
                    for ( let ax = 0; ax < areasize; ax ++ ) {
                        row.push(data[index++]);
                    }
                    area.push(row);
                }
                currentArea.area = area;
                currentArea.status = 2; // loaded
                console.log('loaded (' + areaX + ',' + areaY +')');
            }).fail(function(data) {
                currentArea.status = 3; // error
            });
        }

        return currentArea;
    };

    // 2D Perlin Noise
    module.perlin2 = function(tileX,tileY,x, y) {
        return this.cache(tileX,tileY).perlin(x,y);
    };

    module.load = function(x,y) {
        console.log('precomp length ' + precomp.length);
        if ( precomp.length > 16 ) {
            precomp.shift(1);
        }
        return this.cache(x,y).status;
    }

})(this);
