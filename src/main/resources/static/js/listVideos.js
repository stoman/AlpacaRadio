var app = Vue.createApp({});

app.component('user-details', userDetailsComponent);

const vm = app.mount('#vue');
