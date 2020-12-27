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

function loadNextVideo(player) {
  fetch('/history/current', {headers: getAuthHeaders()})
    .then(function (response) {
      return response.json();
    })
    .then(function (history) {
      player.loadVideoById({
        'videoId': history.video.id,
        'startSeconds': history.startVideoFromSeconds,
        'endSeconds': history.video.endSeconds});
    });
}
