/**
 * Sample JavaScript code for youtube.search.list
 * See instructions for running APIs Explorer code samples locally:
 * https://developers.google.com/explorer-help/code-samples#javascript
 */

function loadClient() {
    gapi.client.setApiKey("AIzaSyBIKC7Qoq2aCB6U_esjbaJdw-fn3BLzACQ");
    return gapi.client.load("https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest")
        .then(function() { console.log("GAPI client loaded for API"); },
            function(err) { console.error("Error loading GAPI client for API", err); });
}
// Make sure the client is loaded before calling this method.
function execute() {
    return gapi.client.youtube.search.list({
        "part": [
            "snippet"
        ],
        "channelId": "UCMGa9iRCmboIJvjH9jMR2fg",
        "maxResults": 10,
        "order": "date",
        "type": [
            "video"
        ]
    })
        .then(function(response) {
            // Handle the results here (response.result has the parsed body).
            // let itemsLength = response.pageInfo.resultsPerPage; // 가져오는 동영상 개수 - maxResults 확인
            let videoList = [];
                console.log("Response", response);
                let itemsArr = response.result.items; // 가져오는 동영상
            for(const element of itemsArr){
                let videoID = element.id.videoId;
                videoList.push(videoID);
            }
            createVideoArea(videoList);
        },
        function(err) { console.error("Execute error", err); });
}

gapi.load("client");

document.addEventListener("DOMContentLoaded",  function () {
    setTimeout(async function(){
            const first = await loadClient();
            const second = await execute();
    },300);
})

function createVideoArea(data) {
    if (data.length == 0) {
        return;
    }

    let playerHtml = document.querySelector('div[id="video_slider"]');
    for (const element of data) {
        //console.log(element);
        let htmlText = "<div class=\"swiper-slide\"><iframe width=\"100%\" height=\"200\" src=\"https://www.youtube.com/embed/" + element + "\"></iframe></div>";
        playerHtml.innerHTML += htmlText;
    }
}