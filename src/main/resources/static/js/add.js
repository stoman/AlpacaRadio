var player;
function onYouTubeIframeAPIReady() {
  player = new YT.Player('player', {
    height: '360',
    videoId: 'weY3e3KNwPw',
    width: '640',
    events: {
      'onStateChange': populateVideoDetails
    }
  });
}

function loadVideo() {
  player.loadVideoById(document.getElementById('videoId').value);
}

function populateVideoDetails(event) {
  if(event.data === YT.PlayerState.PLAYING) {
    var duration = Math.floor(event.target.getDuration());
    document.getElementById('startSeconds').value = 0;
    document.getElementById('startSeconds').max = duration;
    document.getElementById('endSeconds').value = duration;
    document.getElementById('endSeconds').max = duration;
    document.getElementById('title').value = event.target.getVideoData().title;
    document.getElementById('formSecondPart').style.display = 'block';
  }
}
