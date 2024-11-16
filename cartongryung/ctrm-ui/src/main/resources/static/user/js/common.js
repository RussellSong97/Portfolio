/**
 * 이용야관 호출 API
 */
function getTermsService() {
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

/**
 * 컬러코드 : rgb -> hex
 * @param {String} rgbCode  element.style.backgroundColor의 RGB 형식 값
 * @returns {String}        RGB 형식 값 -> 해시코드 형식으로 변환
 *
 */
function colorCode(rgbCode){
    if (rgbCode.search("rgb") == -1 ) {
        return rgbCode;
    } else {
        rgbCode = rgbCode.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+))?\)$/);
        function hex(x) {
            return ("0" + parseInt(x).toString(16)).slice(-2);
        }
        return "#" + hex(rgbCode[1]) + hex(rgbCode[2]) + hex(rgbCode[3]);
    }
}

/**
 * 메뉴 서비스 준비중 alert
 * @returns {Function} 오푼되지 않은 기능을 막아주는 메시지
 */
function fn_serviceWaiting(){
    return alert("서비스 준비 중입니다.");
}

/**
 * 브라우저 창의 너비 가져오기
 * @returns {Number}    브라우저 창의 크기를 정수로 가져옴
 */
function getWindowWidthSize(){
    return window.innerWidth;
}

/**
 * 요소의 너비 가져오기
 * @param {String} 요소 - Event
 * PointerEvent -> Html 추출하기
 */
function getElementWidthByTarget(el){
    const element = convertObjectToHtml(el);

    console.log(element.offsetWidth);
    // return window.innerWidth;
}

/**
 * PointerEvent의 object를 html의 object로 변환
 * @param {Event} obj
 * */
function convertObjectToHtml(obj){
    return obj.target;
}

/**
 * value로 id 가져오기
 * @param {Object} obj
 * @param {String} val
 */
function getKeyByValue(obj, val) {
    return Object.keys(obj).find(key => obj[key] === val);
}

/**
 * 정렬 간접 수정
 * @param {Object} obj  값을 넣을 곳
 * @param {String} key  URLSearchParam에서 가져올 값의 키
 */
function selectSort(obj, key) {
    const searchParam = new URLSearchParams(window.location.search);
    const searchParamKey  = searchParam.get(key);

    if(searchParamKey != "" && searchParamKey != null) {
        selectSortByValue(obj,searchParamKey);
    }
}

/**
 * 정렬 직접 수정
 * @param {Object} obj   값을 넣을 곳
 * @param {String} objVal    URLSearchParam에서 가져온 값
 */
function selectSortByValue(obj, objVal) {
    document.getElementById(obj).value = objVal;
}

/*
function transFormDataToJson(formData) {

    const data = {}
    for (const [name, value] of formData) {
        data[name] = value;
    }
    console.log(data);
}

function transFormDataToJsonKey(formData) {

    const data = {}
    for (const [name, value] of formData) {
        data[name] = value;
    }
    console.log(data);
}*/
