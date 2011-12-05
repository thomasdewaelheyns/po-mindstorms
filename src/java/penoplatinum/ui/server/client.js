(function (globals) {

  // first we define some privates...
  var
  
  queue = [],
  streaming = true,

  lightHistogram = [],
  sonarHistogram = [],

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

  updateLight = function updateLight( lightValue, lightColor ) {
    var v = lightColor / 4;
    updateHTML ( "lightValue", lightValue );
    updateStyle( "lightValue", "backgroundColor", "rgb("+v+","+v+","+v+")" );
    updateHTML ( "lightColor", lightColor );
    updateClass( "lightColor", lightColor );
    addColor   (lightColor);
    renderLightHistogram();
  },

  updateBarcode = function updateBarcode(barcode) {
    updateHTML( "barcode", "barcode: " + barcode );
  },

  updateSonar = function updateSonar(angle, distance) {
    updateHTML( "sonarValue", distance +"cm @ " + angle + "&deg;" );
    addDistance(angle, distance);
    renderSonarHistogram();
  },

  updatePush = function updatePush(left, right) {
    updateHTML ( "push-left", left  ? "PUSHED" : "OK" );
    updateClass( "push-left", left  ? "push active" : "push inactive" );

    updateHTML ( "push-right", right ? "PUSHED" : "OK" );
    updateClass( "push-right", right ? "push active" : "push inactive" );
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
  
  processQueue = function processQueue() {
    // get the next update from the queue and do the update
    if( queue.length > 0 ) {
      var update = queue.shift();

      // time / robot
      updateHTML( "timeStamp", update.timestamp );
      updateHTML( "robotName", update.robot     );
      // model
      updateLight   ( update.lightValue, update.lightColor    );
      updateBarcode ( update.barcode                          );
      updateSonar   ( update.sonarAngle, update.sonarDistance );
      updatePush    ( update.pushLeft,   update.pushRight     );
      // navigator
      updateEvent   ( update.navigatorEvent,  update.navigatorSource      );
      updatePlan    ( update.navigatorAction, update.navigatorActionQueue );
      updateAction  ( update.actionKind,      update.actionArgument       );
      // rate
      updateHTML( "rate", update.rate );
    }
    updateHTML( "queueStatus", "(" + queue.length +")" );
    // schedule the next update in 10ms
    if( streaming ) { setTimeout(processQueue, 10); }
  }

  // then we define a public global namespace/object
  dashboard = globals.Dashboard = {};

  // and expose our public functionality:
  
  // public method used by JSONP to update the dashboard
  dashboard.update = function update( timestamp,       robot,
                                      lightValue,      lightColor, 
                                      barcode,
                                      sonarAngle,      sonarDistance,
                                      pushLeft,        pushRight,
                                      navigatorEvent,  navigatorSource,
                                      navigatorAction, navigatorActionQueue,
                                      actionKind,      actionArgument,
                                      rate )
  {
    // add the update to the queue ...
    queue.push( {
      timestamp : timestamp,             robot : robot,
      lightValue : lightValue,           lightColor : lightColor,
      barcode : barcode,
      sonarAngle : sonarAngle -90,       sonarDistance : sonarDistance,
      pushLeft : pushLeft,               pushRight : pushRight,
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

}(window));
