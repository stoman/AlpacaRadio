function onYouTubeIframeAPIReady() {
  var player = new YT.Player('player', {
    height: '360',
    videoId: 'weY3e3KNwPw',
    width: '640',
    playerVars: {
      'autoplay': 1,
      'controls': 0
    },
    events: {
      'onReady': onPlayerReady,
      'onStateChange': onPlayerStateChange
    }
  });
}

function onPlayerReady(event) {
  loadNextVideo(event.target);
}

function onPlayerStateChange(event) {
  if (event.data == YT.PlayerState.PLAYING) {
    document.getElementById('autoplay-disabled').style.display = 'none';
  }
  if (event.data == YT.PlayerState.ENDED) {
    loadNextVideo(event.target);
  }
}

var loadNextVideoLastCall = new Date(1970, 0)
function loadNextVideo(player) {
  var current = new Date()
  if(current - loadNextVideoLastCall <= 1000) {
    return;
  }
  loadNextVideoLastCall = current;

  fetch('/history/current', {headers: getAuthHeaders()})
    .then(function (response) {
      return response.json();
    })
    .then(function (response) {
      player.loadVideoById({
        'videoId': response.videoId,
        'startSeconds': response.startFromSeconds,
        'endSeconds': response.endSeconds});
      document.getElementById('addedByName').innerHTML = response.addedByName;
      document.getElementById('addedByPicture').src = response.addedByPicture;
    });
}
