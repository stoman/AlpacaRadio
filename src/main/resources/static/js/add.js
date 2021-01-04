var app = Vue.createApp({
  data() {
    return {
      videoLoaded: false,
      videoId: '',
      startSeconds: 0,
      startSecondsMax: 0,
      endSeconds: 0,
      endSecondsMax: 0,
      title: '',
      ytPlayer: undefined,
    }
  },
  methods: {
    loadVideo() {
      if(!!this.ytPlayer) {
        this.ytPlayer.loadVideoById(this.videoId);
      }
      else {
        this.ytPlayer = new YT.Player('player', {
          height: '360',
          videoId: this.videoId,
          width: '640',
          events: {
           'onStateChange': this.populateVideoDetails
          },
          playerVars: {
            autoplay: 1,
          },
        });
      }
    },
    populateVideoDetails(event) {
      if(event.data !== YT.PlayerState.ENDED) {
        var duration = Math.floor(event.target.getDuration());
        this.startSeconds = 0;
        this.startSecondMax = duration;
        this.endSeconds = duration;
        this.endSecondsMax = duration;
        this.title = event.target.getVideoData().title;
        this.videoLoaded = true;
      }
    },
  },
});

const vm = app.mount('#vue');
