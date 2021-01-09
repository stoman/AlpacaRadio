var app = Vue.createApp({
  data() {
    return {
      videoId: '',
      videoTitle: 'Loading radio station...',
      addedByName: '',
      addedByPicture: '',
      userVote: '',
      userVoteServer: '',
      userVoteUpdating: false,
      votes: {},
      videoWarning: false,
      ytPlayer: undefined,
      loadNextVideoLastCall: new Date(1970, 0),
    }
  },
  watch: {
    userVote: function(newVote, oldVote) {
      if(newVote == this.userVoteServer) {
        return;
      }

      this.votes[newVote]++;
      this.votes[oldVote]--;
      this.userVoteServer = newVote;
      this.userVoteUpdating = true;
      var that = this;
      var headers = getAuthHeaders();
      headers.append('Content-Type', 'application/json');
      fetch('/api/addVote', {
        headers: headers,
        method: 'POST',
        body: JSON.stringify({videoId: this.videoId, voteType: newVote})
      })
      .then(function(response) {
        that.userVoteUpdating = false;
      });
    }
  },
  methods: {
    onPlayerStateChange(event) {
      if (event.data === YT.PlayerState.PLAYING) {
        this.videoWarning = false;
      }
      if (event.data === YT.PlayerState.ENDED) {
        this.loadNextVideo();
      }
    },
    loadNextVideo() {
      // Only call this at most once per second.
      var current = new Date()
      if(current - this.loadNextVideoLastCall <= 1000) {
        return;
      }
      this.loadNextVideoLastCall = current;

      // Load next video.
      var that = this;
      fetch('/api/getCurrentVideo', {headers: getAuthHeaders()})
        .then(function (response) {
          return response.json();
        })
        .then(function (response) {
          that.videoId = response.videoId;
          that.videoTitle = response.videoTitle;
          that.addedByName = response.addedByName;
          that.addedByPicture = response.addedByPicture;
          that.userVoteServer = response.userVote;
          that.userVote = response.userVote;
          that.votes = response.votes;

          if(!!that.ytPlayer) {
            that.ytPlayer.loadVideoById({
              videoId: response.videoId,
              startSeconds: response.startFromSeconds,
              endSeconds: response.endSeconds});
          }
          else {
            that.ytPlayer = new YT.Player('player', {
              height: '360',
              width: '640',
              videoId: response.videoId,
              playerVars: {
                autoplay: 1,
                controls: 0,
                start: response.startFromSeconds,
                end: response.endSeconds
              },
              events: {
                onStateChange: that.onPlayerStateChange
              }
            });
            that.videoWarning = true;
          }
        });
    },
  },
})

app.component('user-details', userDetailsComponent);

const vm = app.mount('#vue');

function onYouTubeIframeAPIReady() {
  vm.loadNextVideo();
}
