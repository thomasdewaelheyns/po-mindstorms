// 
function updateHTML(id, value) { 
  document.getElementById(id).innerHTML = value;
}

// public method used by JSONP to update the dashboard
function update( lightValue,      lightColor, 
                 sonarAngle,      sonarDistance,
                 pushLeft,        pushRight,
                 navigatorEvent,  navigatorSource,
                 navigatorAction, navigatorActionQueue,
                 actionKind,      actionArgument )
{
  // TODO
}

function renderLightHistogram(hist) {
  var canvas = document.getElementById("lightHistogram").getContext("2d");
  for( var i=0; i<hist.length; i++ ) {
    canvas.fillStyle = ( hist[i] == 1 ? "rgb(209,127,46)" : ( hist[i] == 0 ? "black" : "white" ) );
    canvas.fillRect(i*5,0,5,70);
  }
}

function renderSonarHistogram(hist) {
  var canvas = document.getElementById("sonarHistogram").getContext("2d");
  var opacity = 1;
  canvas.fillStyle = "black";
  canvas.fillRect(0,0,150,150);
  for( var i=0; i<hist.length; i++ ) {
    var angle = hist[i][0];
    var dist  = hist[i][1];
    canvas.strokeStyle = "rgba(0,255,0," + opacity + ")";
    canvas.beginPath();
    canvas.moveTo(75,125);
    canvas.lineTo(  75 - Math.sin((angle)/180*Math.PI) * dist, 
    125 - Math.cos((angle)/180*Math.PI) * dist );
    canvas.stroke();
    opacity -= 0.1   ;
  }
}

// mock-up demo
renderLightHistogram( [ 1,1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 ] );
renderSonarHistogram( [ [35,74], [25,65], [15,60], [5,59], [-5,255], [-15,255], [-25,255], [-35,255], [-45,255], [-55,255], [-65,255], [-75,255], [-85,255] ] );
