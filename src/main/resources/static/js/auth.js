function getXsrfToken() {
  const tokenCookie = document.cookie.split('; ').find(row => row.startsWith('XSRF-TOKEN'));
  if(!tokenCookie) {
    return undefined;
  }
  return tokenCookie.split('=')[1];
}

function getAuthHeaders() {
  const headers = new Headers();
  const xsrfToken = getXsrfToken();
  if(!!xsrfToken) {
    headers.append('X-XSRF-TOKEN', xsrfToken);
  }
  return headers;
}

function logout() {
  fetch('/logout', {headers: getAuthHeaders(), method: 'POST'})
    .then(function(response) {
        window.location = response.url;
    });
  return false;
}

const userDetailsComponent = {
  props: ['name', 'picture'],
  template: `<span class="user">
   <img v-bind:src="picture" style="width: 2em; height: 2em; border-radius: 0.5em;" />
   {{name}}
 </span>`
}
