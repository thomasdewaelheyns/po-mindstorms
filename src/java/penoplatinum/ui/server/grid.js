// The Grid Module
(function(globals) {

  // provides a instantiateable Grid
  function construct(id) {
    // first we define some privates...
    var

    parent, node, canvas,

    sectors = {}, agents = {},
    
    minLeft = 0, maxLeft = 0,
    minTop  = 0, maxTop  = 0,
    
    N = 0x1, E = 0x2, S = 0x4, W = 0x8,
    
    sectorSize = 15,
    wallWidth  = 3,

    get = function get(left, top) {
      var key = left + "," + top;
      return sectors[key];
    },
    
    getWidth  = function getWidth()  { return maxLeft - minLeft + 1; },
    getHeight = function getHeight() { return maxTop  - minTop  + 1; },

    getNode = function getNode() {
      if(!node) { node   = document.getElementById(id); }
      return node;
    },

    getCanvas = function getCanvas() {
      if(!canvas) { canvas = getNode().getContext("2d"); }
      return canvas;
    },

    getParent = function getParent() {
      if(!parent) { parent = getNode().parentNode; }
      return parent;
    },

    getCanvasWidth  = function getCanvasWidth()  { 
      return getWidth() * sectorSize;
    },
    
    getCanvasHeight = function getCanvasHeight() {
      return getHeight() * sectorSize;
    },
    
    getParentWidth = function getParentWidth() {
      return getParent().offsetWidth;
    },

    getParentHeight = function getParentHeight() {
      return getParent().offsetHeight;
    },
    
    setNodeLeft = function setNodeLeft(left) {
      if( left < 0 ) { left = 0; }
      getNode().style.left = left + "px";
    },
    
    setNodeTop = function setNodeTop(top) {
      if( top < 0 ) { top = 0; }
      getNode().style.top = top +"px";
    },
    
    center = function center() {
      setNodeLeft((getParentWidth() - getCanvasWidth())  / 2 -4); // -4
      setNodeTop((getParentHeight() - getCanvasHeight()) / 2 -4); // borders
    },
    
    resize = function resize() {
      getNode().width  = getCanvasWidth();
      getNode().height = getCanvasHeight();
      center();
    },
    
    clear = function clear() {
      getCanvas().fillStyle = "rgb(150,150,150)";
      getCanvas().fillRect(0, 0, getCanvas().width, 
                                 getCanvas().height);
    },
    
    getSector = function getSector(left, top) {
      return sectors[left+","+top];
    },
    
    renderSector = function renderSector(left, top, value) {
      getCanvas().fillStyle = valueToColor(value);
      getCanvas().fillRect((left-minLeft) * sectorSize + 1,
                           (top-minTop)   * sectorSize + 1,
                            sectorSize - 2, sectorSize - 2);
    },

    renderWalls = function renderWalls(left, top, sector) {
      getCanvas().fillStyle = "rgb(255, 255, 255)";
      if( sector.hasNorth ) {
        getCanvas().fillRect((left-minLeft) * sectorSize,
                             (top-minTop) * sectorSize,
                             sectorSize, wallWidth);
      }
      if( sector.hasEast ) {
        getCanvas().fillRect((left-minLeft+1) * sectorSize - wallWidth,
                             (top-minTop) * sectorSize,
                             wallWidth, sectorSize);
      }
      if( sector.hasSouth ) {
        getCanvas().fillRect((left-minLeft) * sectorSize,
                             (top-minTop + 1) * sectorSize - wallWidth,
                             sectorSize, wallWidth);
      }
      if( sector.hasWest ) {
        getCanvas().fillRect((left-minLeft) * sectorSize,
                             (top-minTop) * sectorSize,
                             wallWidth, sectorSize);
      }
    },
    
    renderSectors = function renderSectors() {
      for(var top=minTop; top<=maxTop; top++) {
        for(var left=minLeft; left<=maxLeft; left++) {
          var sector = get(left, top);
          if( sector ) {
            // render a sector
            renderSector(left, top, sector.value);
            renderWalls(left, top, sector);
          }
        }
      }
    },
    
    renderAgent = function renderAgent(left, top, bearing, color) {
      getCanvas().save();
      left = (left - minLeft) * sectorSize + sectorSize/2;
      top  = (top  - minTop)  * sectorSize + sectorSize/2;
      getCanvas().translate(left, top);
      getCanvas().rotate((bearing-1)*Math.PI/2);
      getCanvas().fillStyle = color;
      getCanvas().beginPath();
      var s = sectorSize/3;
      getCanvas().moveTo(   0,-1*s);
      getCanvas().lineTo(   s,   s);
      getCanvas().lineTo(-1*s,   s);
      getCanvas().closePath();
      getCanvas().fill();
      getCanvas().restore();
    },
    
    renderAgents = function renderAgents() {
      for(var name in agents) {
        renderAgent(agents[name].left,    agents[name].top, 
                    agents[name].bearing, agents[name].color);
      }
    },

    render = function render() {
      resize();
      clear();
      renderSectors();
      renderAgents();
    },
    
    valueToColor = function valueToColor(value) {
      if( value == 0 ) { return "rgb(0,0,0)"; }
      value *= 2;
    
      var r, g, b, v = value;
      v = (v/1000) * 400 + 350;

      if(v>=350 && v<=439) {            // violet
        r = (440-v) * 255 / 90;
        g = 0;
        b = 255;
      } else if( v>=440 && v<=489 ) {   // blue
        r = 0;
        g = (v-440) * 255 / 50;
        b = 255;
      } else if( v>=490 && v<=509 ) {   // cyan
        r = 0;
        g = 255;
        b = (510-v) * 255 / 20;
      } else if ( v>=510 && v<=579 ) {  // green
        r = (v-510) * 255 / 70;
        g = 255;
        b = 0;
      } else if( v>=580 && v<=644 ) {   // yellow
        r = 255;
        g = (645-v) * 255 / 65;
        b = 0;
      } else if( v>=645 ) {   // red
        r = 255;
        g = 0;
        b = 0;
      } else {                          // black
        r = 0;
        g = 0;
        b = 0;
      }
      return "rgb("+Math.round(r)+","+Math.round(g)+","+Math.round(b)+")";
    }

    // then we turn the provided variable into a Grid object
    var grid = {};

    // and add our public functionality:

    // public method add sectors
    grid.updateWalls = function updateWalls(walls) {
      // copy the new info into the private sectors hash
      for(var key in walls) {
        // update boundaries
        var pos  = key.split(","), 
            left = parseInt(pos[0]),
            top  = parseInt(pos[1]);
        if(left < minLeft) { minLeft = left; }
        if(left > maxLeft) { maxLeft = left; }
        if(top  < minTop)  { minTop  = top;  }
        if(top  > maxTop)  { maxTop  = top;  }
        // store the (new) wall info
        sectors[key] = {
          hasNorth : (walls[key] & N),
          hasEast  : (walls[key] & E),
          hasSouth : (walls[key] & S),
          hasWest  : (walls[key] & W),
          value    : 0
        }
      }
      // and render the new grid info
      render();
    }
    
    grid.updateValues = function updateValues(values) {
      // copy the new info into the private sectors hash
      for(var key in values) {
        if(sectors[key]) { // we only track values for sectors we know
          sectors[key].value = values[key];
        }
      }
      // and render the new grid info
      render();
    }
    
    grid.updateAgents = function updateAgents(info) {
      // copy the new info into the private agents hash
      for(var name in info) {
        agents[name] = info[name];
      }
      // and render the new grid info
      render();
    }
    
    return grid;
  }
  
  Grid = globals.Grid = {};
  
  // maps external create factory method to inner construct method
  Grid.create   = function create(id) { return construct(id); }
  Grid.getValue = function getValue(left, top) {
    var sector = getSector(left, top);
    return sector ? sector.value : 0;
  }

})(window);
