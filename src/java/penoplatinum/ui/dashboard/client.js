(function (globals) {

  // first we define some privates...
  var xmlhttp,

      initAjax = function initAjax() {
        xmlhttp = null;
        if(window.XMLHttpRequest) {
          // code for IE7+, Firefox, Chrome, Opera, Safari
          xmlhttp=new XMLHttpRequest();
        } else if (window.ActiveXObject) {
          // code for IE6, IE5
          xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        } else {
          alert("Your browser does not support XMLHTTP!");
        }
      },

      ajaxFetch = function ajaxFetch(url, callback) {
        xmlhttp.open("GET", url, false );
        xmlhttp.onreadystatechange = function () {
		      if (xmlhttp.readyState != 4) return;
		      if (xmlhttp.status != 200 && xmlhttp.status != 304 && xmlhttp.status != 0) {
            console.log('HTTP error ' + xmlhttp.status);
			      return;
		      }
		      callback(xmlhttp.responseText);
	      }
        xmlhttp.send(null);
      },
      
      REFRESH_INTERVAL_LOCAL_FILE = 250,
      continueProcessingFile = false,

      currentRobot = "",

  queue = [],
  streaming = true,

  // TODO: fix sharing with grid.js ;-)
  N = 0x1, E = 0x2, S = 0x4, W = 0x8,

  lightHistogram = [],
  sonarHistogram = [],
  
  static_id = 0,
  static_file = "updates-1.js",   // init needs to be synced with Gateway cfg
  
  clearDashboard = function clearDashboard() {
    lightHistogram = [];
    sonarHistogram = [];
    queue = [];
    updateLight   ( "&nbsp;", "&nbsp;", "&nbsp;" );
    updateBarcode ( "&nbsp;" );
    updateSonar   ( " ", " " );
    updateIR      ( " ", " " );
    updateSector  ( " ", " " );
    // navigator
    updateEvent   ( " ", " " );
    updatePlan    ( " ", " " );
    updateAction  ( " ", " " );

    var canvas = document.getElementById("lightHistogram").getContext("2d");
    canvas.fillStyle = "white";
    canvas.fillRect(0,0,500,70);
  }

  // helper functions to update HTML parts
  updateHTML = function updateHTML(id, value) { 
    document.getElementById(id).innerHTML = value;
  },

  updateStyle = function updateStyle(id, style, value ) {
    document.getElementById(id).style[style] = value;
  },

  updateClass = function updateClass(id, className) {
    document.getElementById(id).className = className;
  },

  updateLight = function updateLight( lightValue, lightColor, avgLightValue ) {
    var v = lightColor / 4;
    updateHTML ( "lightValue", lightValue + " / " + avgLightValue );
    updateStyle( "lightValue", "backgroundColor", "rgb("+v+","+v+","+v+")" );
    updateHTML ( "lightColor", lightColor );
    updateClass( "lightColor", lightColor );
    addColor   (lightColor);
    renderLightHistogram();
  },

  updateBarcode = function updateBarcode(barcode) {
    updateHTML( "barcode", "barcode: " + ( barcode > 0 ? barcode : "" ) );
  },

  updateSonar = function updateSonar(angle, distance) {
    updateHTML( "sonarValue", distance +"cm @ " + angle + "&deg;" );
    addDistance(angle, distance);
    renderSonarHistogram();
  },

  updateEvent = function updateEvent( kind, source ) {
    kind   = typeof kind != "undefined" ? kind : "";
    source = typeof source != "undefined" ? source : "";
    updateHTML( "eventKind",   kind );
    updateHTML( "eventSource", "on " + source );
  },

  updatePlan = function updatePlan( action, actionQueue ) {
    updateHTML( "eventAction", action );
    if( typeof actionQueue == "undefined" ) { return; }
    var actions = actionQueue.split("|");
    var queueHTML = "";
    for( var action in actions ) {
      queueHTML += "<li>" + actions[action] + "</li>";
    }
    updateHTML( "eventActionQueue", queueHTML );
  },

  updateAction = function updateAction( kind, argument ) {
    updateHTML( "actionKind", kind );
    updateHTML( "actionArgument", argument );
  },

  addColor = function addColor(color) {
    lightHistogram.unshift(color);
    // clean up acient history, keep max values
    var max = 100;
    if( lightHistogram.length > max ) {
      lightHistogram.splice(max, lightHistogram.length - max);
    }
  },

  addDistance = function addDistance(angle, distance) {
    sonarHistogram.unshift([angle,distance]);
    // clean up acient history, keep max values
    var max = 15;
    if( sonarHistogram.length > max ) {
      sonarHistogram.splice(max, sonarHistogram.length - max);
    }
  },

  renderLightHistogram = function renderLightHistogram() {
    var canvas = document.getElementById("lightHistogram").getContext("2d");
    for( var i=0; i<lightHistogram.length; i++ ) {
      canvas.fillStyle = ( lightHistogram[i] == "brown" ? "rgb(209,127,46)" : 
      lightHistogram[i] );
      canvas.fillRect(i*5,0,5,70);
    }
  },

  renderSonarHistogram = function renderSonarHistogram(hist) {
    var canvas = document.getElementById("sonarHistogram").getContext("2d");
    var opacity = 1;
    canvas.fillStyle = "black";
    canvas.fillRect(0,0,150,150);
    for( var i=1; i<sonarHistogram.length; i++ ) {
      var angle1 = sonarHistogram[i][0];
      var dist1  = sonarHistogram[i][1];
      var angle2 = sonarHistogram[i-1][0];
      var dist2  = sonarHistogram[i-1][1];
      canvas.beginPath();
      canvas.strokeStyle = "rgba(0,255,0," + opacity + ")";
      canvas.fillStyle   = "rgba(0,255,0," + opacity + ")";
      canvas.moveTo(75,125);
      canvas.lineTo( 75 - Math.sin((angle1)/180*Math.PI) * dist1,
                    125 - Math.cos((angle1)/180*Math.PI) * dist1 );
      canvas.lineTo( 75 - Math.sin((angle2)/180*Math.PI) * dist2,
                    125 - Math.cos((angle2)/180*Math.PI) * dist2 );
      canvas.moveTo(75,125);
      canvas.stroke();
      canvas.fill();
      opacity -= 0.1   ;
    }
  },
  
  updateIR = function updateIR(values, dist) {
    updateHTML("IrValue", dist+"cm");

    var canvas = document.getElementById("irField").getContext("2d");
    canvas.fillStyle = "black";
    canvas.fillRect(0,0,150,150);

    canvas.fillStyle = "white";
    canvas.fillRect(10,60,10,10);
    canvas.fillRect(70,30,10,10);
    canvas.fillRect(130,60,10,10);
    canvas.fillRect(10,120,10,10);
    canvas.fillRect(130,120,10,10);

    canvas.beginPath();
    canvas.strokeStyle = "rgba(255,0,0,1)";
    canvas.fillStyle   = "rgba(255,0,0,0.4)";
    canvas.moveTo(75,125);
    if( values[0] > 0 ) {
      canvas.lineTo(15,125); canvas.lineTo(15,65); canvas.lineTo(75,125);
      canvas.fillRect(11,121,8,8); canvas.fillRect(11,61,8,8);
    } else if( values[1] > 0 ) {
      canvas.lineTo(15,65); canvas.lineTo(75,35); canvas.lineTo(75,125);
      canvas.fillRect(11,61,8,8);  canvas.fillRect(71,31,8,8);
    } else if( values[2] > 0 ) {
      canvas.lineTo(75,35); canvas.lineTo(135,65); canvas.lineTo(75,125);
      canvas.fillRect(71,31,8,8);  canvas.fillRect(131,61,8,8);
    } else if( values[3] > 0 ) {
      canvas.lineTo(135,65); canvas.lineTo(135,125); canvas.lineTo(75,125);
      canvas.fillRect(131,51,8,8);  canvas.fillRect(131,121,8,8);
    }
    canvas.closePath();
    canvas.stroke();
    canvas.fill();
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
  },
  
  updateSector = function updateSector( walls, values ) {
    var canvas = document.getElementById("sectorSituation").getContext("2d");
    canvas.fillStyle = "black";
    canvas.fillRect(0,0,150,150);

    // sectors
    // N
    canvas.fillStyle = valueToColor(values.N);
    canvas.fillRect( 60, 10, 30, 30 );

    // W
    canvas.fillStyle = valueToColor(values.W);
    canvas.fillRect( 10, 60, 30, 30 );

    // Current
    canvas.fillStyle = "white";
    canvas.fillRect( 60, 60, 30, 30 );

    // E
    canvas.fillStyle = valueToColor(values.E);
    canvas.fillRect( 110, 60, 30, 30 );

    // S
    canvas.fillStyle = valueToColor(values.S);
    canvas.fillRect( 60, 110, 30, 30 );
    
    // walls
    canvas.fillStyle = "brown";

    if( walls & N ) { canvas.fillRect(50,47,50,5); }
    if( walls & E ) { canvas.fillRect(95,50,5,50); }
    if( walls & S ) { canvas.fillRect(50,97,50,5); }
    if( walls & W ) { canvas.fillRect(50,47,5,50); }
  },
  
  processQueue = function processQueue() {
    // get the next update from the queue and do the update
    if( queue.length > 0 ) {
      var update = queue.shift();

      // time / robot
      updateHTML( "timeStamp", update.timestamp );
      // model
      updateLight   ( update.lightValue, update.lightColor, update.avgLightValue );
      updateBarcode ( update.barcode                          );
      updateSonar   ( update.sonarAngle, update.sonarDistance );
      updateIR      ( update.ir, update.irDist );
      updateSector  ( update.walls, update.values );
      // navigator
      updateEvent   ( update.navigatorEvent,  update.navigatorSource      );
      updatePlan    ( update.navigatorAction, update.navigatorActionQueue );
      updateAction  ( update.actionKind,      update.actionArgument       );
      // rate
      // updateHTML( "rate", update.rate + "fps" );
    }
    updateHTML( "queueStatus", "(" + queue.length +")" );
    // schedule the next update in 30ms
    if( streaming || continueProcessingFile ) { setTimeout(processQueue, 30); }
  },
  
  refreshRobots = function refreshRobots() {
    ajaxFetch( "./robots", function(data) {
      var html = "<option value=\"stop\">selecteer een robot...</option>";
      var robots = data.split("\n");
      for(var r in robots) {
        if( robots[r] != "" )  {
          html += '<option value="'+robots[r]+'"' + 
          (currentRobot == robots[r] ? 'selected' : '') + '>' + 
          robots[r]+'</option>';
        }
      }
      document.getElementById("robotSelection").innerHTML = html;
    } );
  },

  processFile = function processFile() {
    var file = "./"+ static_file +"?" + (Math.floor(Math.random()*10000));
    var headID = document.getElementsByTagName("head")[0];         
    var newScript = document.createElement('script');
    newScript.type = 'text/javascript';
    newScript.src = file;
    headID.appendChild(newScript);
    if(continueProcessingFile) {
      setTimeout( processFile, REFRESH_INTERVAL_LOCAL_FILE );
    }
    
    // DOESN'T WORK ON LOCAL FS   :-(
    // ajaxFetch( "./updates.js?" + (Math.floor(Math.random()*10000)),
    //   function(data) {
    //     try { 
    //       eval(data);
    //       if(continueProcessingFile) {
    //         setTimeout( processFile, REFRESH_INTERVAL_LOCAL_FILE );
    //       }
    //     } catch(err) {
    //       console.log(err);
    //     }
    //   } );
  },

  // then we define a public global namespace/object
  dashboard = globals.Dashboard = {};

  // and expose our public functionality:
  
  // public method used by JSONP to update the dashboard
  dashboard.update = function update( timestamp,  robot,
                                      lightValue, lightColor, avgLightValue,
                                      barcode,
                                      sonarAngle, sonarDistance,
                                      ir1, ir2, ir3, ir4, ir5, irDist,
                                      walls, valN, valE, valS, valW,
                                      navigatorEvent,  navigatorSource,
                                      navigatorAction, navigatorActionQueue,
                                      actionKind, actionArgument,
                                      rate )
  {
    // add the update to the queue ...
    queue.push( {
      timestamp : timestamp,             robot : robot,
      lightValue : lightValue,           lightColor : lightColor, avgLightValue : avgLightValue,
      barcode : barcode,
      sonarAngle : sonarAngle,       sonarDistance : sonarDistance,
      ir : [ ir1, ir2, ir3, ir4, ir5 ],  irDist: irDist,
      walls:walls,  values : {N:valN, E:valE, S:valS, W:valW },
      navigatorEvent : navigatorEvent,   navigatorSource : navigatorSource,
      navigatorAction : navigatorAction, navigatorActionQueue : navigatorActionQueue,
      actionKind : actionKind,           actionArgument : actionArgument,
      rate:rate
    } );
  };

  // public method used by JSONP to display an error message
  dashboard.error = function error( msg ) {
    alert( "ERROR: " + msg );
  };

  dashboard.play = function play() {
    streaming = true;
    processQueue();
  };

  dashboard.pause = function pause() {
    streaming = false;
  };

  dashboard.step = function step() {
    processQueue();
  };

  // public method used by JSONP to start the stream
  dashboard.start = function start() {
    // start an event loop that processes the queue
    dashboard.play();
  };
  
  // initialize our own Grid and expose it using a public variable
  dashboard.myGrid = globals.Grid.create("myGrid");
  // initialize Grids for the other
  dashboard.others = [];
  dashboard.others[0] = globals.Grid.create("others1");
  dashboard.others[1] = globals.Grid.create("others2");
  dashboard.others[2] = globals.Grid.create("others3");
  
  dashboard.useSource = function useSource(source) {
    clearDashboard();
    document.getElementById("robotSelection").style.display = "none";
    continueProcessingFile = false;
    if(source == "file") {
      continueProcessingFile = true;
      static_id = 0;
      processFile();
      processQueue();
    } else if(source == "server" ) {
      document.getElementById("robotSelection").style.display = "inline";
      refreshRobots();
    }
  }
  
  dashboard.showRobot = function showRobot(robot) {
    clearDashboard();
    document.getElementById("iframe").src = "./replay?robot=" + robot;
  }
  
  // support for updates through "static" files
  // 534,"Ghost1",500,"BROWN",500,-1,90,18,0,0,0,0,0,-1,6,856,3750,0,3750,"","","","","","",0
  dashboard.file_update_model = function file_update_model(id,  robot,
                              lightValue, lightColor, avgLightValue,
                              barcode,
                              sonarAngle, sonarDistance,
                              ir1, ir2, ir3, ir4, ir5, irDist,
                              walls, valN, valE, valS, valW,
                              navigatorEvent,  navigatorSource,
                              navigatorAction, navigatorActionQueue,
                              actionKind, actionArgument,
                              rate)
  {
    if( id < static_id ) { return; }
    // console.log(static_id, id,  robot,
    //             lightValue, lightColor, avgLightValue,
    //             barcode,
    //             sonarAngle, sonarDistance,
    //             ir1, ir2, ir3, ir4, ir5, irDist,
    //             walls, valN, valE, valS, valW,
    //             navigatorEvent,  navigatorSource,
    //             navigatorAction, navigatorActionQueue,
    //             actionKind, actionArgument,
    //             rate);
    dashboard.update(id,  robot,
                          lightValue, lightColor, avgLightValue,
                          barcode,
                          sonarAngle, sonarDistance,
                          ir1, ir2, ir3, ir4, ir5, irDist,
                          walls, valN, valE, valS, valW,
                          navigatorEvent,  navigatorSource,
                          navigatorAction, navigatorActionQueue,
                          actionKind, actionArgument,
                          rate );
    static_id = id;
  }
  
  dashboard.file_update_walls = function file_update_walls(id, robot, grid, left, top, wallsConfig) {
    if( id < static_id ) { return; }
    var pos = left + "," + top
    var walls = {}; values[pos] = wallsConfig;
    dashboard[grid].updateWalls(id, robot, walls);
    static_id = id;
  }

  dashboard.file_update_values = function file_update_values(id, robot, grid, left, top, value) {
    if( id < static_id ) { return; }
    var pos = left + "," + top
    var values = {}; values[pos] = value;
    dashboard[grid].updateValues(id, robot, values );
    static_id = id; 
  }

  dashboard.file_update_agents = function file_update_agents(id, robot, grid, agent, l, t, bearing, color) {
    if( id < static_id ) { return; }
    dashboard[grid].updateAgents(id, robot, { name: agent, left: l, top: t });
    static_id = id;
  }
  
  // start following the other file
  dashboard.swap = function swap(nextFileName) {
    console.log("swap -> " + nextFileName);
    static_file = nextFileName;
  }
  
  initAjax();
}(window));
