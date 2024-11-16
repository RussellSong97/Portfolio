/**
 * 이용야관 호출 API
 */
function getTermsService() {
    console.log("getTermsService");
    fetch("/api/terms/service", {
        method:"GET"
    }).then(function(response) {
        return response.text();
    }).then(function(data) {
        document.getElementById("termsPopup").querySelector(".popup-text").innerHTML = data;
    }).catch(function (error) {
        console.log(error);
    });
}

/**
 * 개인정보처리방침 호출 API
 */
function getTermsPrivacy() {
    console.log("getTermsService");
    fetch("/api/terms/privacy", {
        method:"GET"
    }).then(function(response) {
        return response.text();
    }).then(function(data) {
        document.getElementById("policyPopup").querySelector(".popup-text").innerHTML = data;
    }).catch(function (error) {
        console.log(error);
    });
}