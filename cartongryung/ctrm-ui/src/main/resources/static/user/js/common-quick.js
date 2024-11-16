/**
* 간편 상담 신청에 대한 로직
 * 1) m- 모바일
 * 2) pc- 컴퓨터
 * 3) fin - 금융 연락처 남기기
* */

/**
 * (공통)간편 상담 신청 - 체크박스 값 변경
 * @param {HTMLDocument}    element 변경할 HTML 체크박스 value
 * */
const changeQuickApplyCheckBox = (element) => {
    if(element.id.indexOf("-terms01") < 0){
        return;
    }
    element.value = "N";

    if(element.checked){
        element.value = "Y";
    }
}

/**
 * (공통)간편 상담 신청 - 점검
 * @param {String} elName   금융, 푸터(모바일), 푸터(컴퓨터)
 * @returns {Boolean}       필수 기입란 결함 유무(무:false)
 * */
const validCommonQuickAsk = (elName) => {
    const termCheckBox = document.getElementById(elName + "terms01");             // 개입정보 수집 및 동의 체크란
    const costumerName = document.getElementById(elName + "costumerName");        // 고객명
    const costumerNumber = document.getElementById(elName + "costumerNumber");    // 고객번호
    const costumerAsk = document.getElementById(elName + "costumerAsk");          // 문의 내용

    if(!termCheckBox.checked){
        return true;
    }
    if(costumerName.value === ""){
        return true;
    }
    if(costumerNumber.value === ""){
        return true;
    }
    if(costumerAsk.value === ""){
        return true;
    }
    return false;
}

/**
 * (공통)간편 상담 신청 - 점검
 * @param {String} elName   금융, 푸터(모바일), 푸터(컴퓨터)
 * @returns {String}        필수 기입란 결함 메시지
 * */
const validCommonQuickAskToMsg = (elName) => {
    const termCheckBox = document.getElementById(elName + "terms01");             // 개입정보 수집 및 동의 체크란
    const costumerName = document.getElementById(elName + "costumerName");        // 고객명
    const costumerNumber = document.getElementById(elName + "costumerNumber");    // 고객번호
    const costumerAsk = document.getElementById(elName + "costumerAsk");          // 문의 내용

    if(costumerName.value === ""){
        costumerName.focus();
        return "이름 작성해 주시길 바랍니다.";
    }

    if(costumerNumber.value === ""){
        costumerNumber.focus();
        return "전화 번호를 작성해 주시길 바랍니다.";
    }

    if(costumerAsk.value.trim() === ""){
        costumerAsk.focus();
        return "문의 내용을 작성해 주시길 바랍니다.";
    }

    if(!termCheckBox.checked){
        return "개인정보 수집 및 동의란에 체크해 주시길 바랍니다.";
    }

    return false;
}

/**
 * (공통)간편 상담 신청 - 상담 신청 버튼 활성화
 * 활성 전 = 회색 > 활성 후 = 붉은 색
 * @param {String}  elName  금융, 푸터(모바일), 푸터(컴퓨터)
 * @param {String}  isClear 필수 기입 애로사항(false:무)
 */
const activateCommonQuickApplyBtn = (elName, isClear) => {
    let applyQuickBtn = document.getElementById(elName + "quick-apply-btn");

    if(!isClear){
        applyQuickBtn.style.backgroundColor = "#222222";
        applyQuickBtn.style.border = "1px solid #222222";
    }else{
        applyQuickBtn.style.backgroundColor = "#DDDDDD";
        applyQuickBtn.style.border = "1px solid #DDDDDD";
    }
}

/**
 * (공통)간편 상담 신청 - 상담 신청 버튼 활성화
 * 활성 전 = 회색 > 활성 후 = 붉은 색
 * @param {String}  fstDivName  금융, 푸터(모바일), 푸터(컴퓨터)
 * @returns {Boolean}           푸터(컴퓨터)인 경우는 활성 기능 없으므로 false, 나머지는 true
 */
const isActivateCommonQuickApplyBtn = (fstDivName) => {
    if(fstDivName.indexOf("pc-") > -1){
        return false;
    }
    return true;
}

/**
 * (공통)간편 상담 신청 초기화
 * @param {String}  elName  금융, 푸터(모바일), 푸터(컴퓨터)
 * */
const resetCommonQuickAsk = (elName) => {
    let termCheckBox = document.getElementById(elName + "terms01");
    let costumerName = document.getElementById(elName + "costumerName");
    let costumerNumber = document.getElementById(elName + "costumerNumber");
    let costumerAsk = document.getElementById(elName + "costumerAsk");

    termCheckBox.checked = false;
    costumerName.value = "";
    costumerNumber.value = "";
    costumerAsk.value = "";

    changeQuickApplyCheckBox(termCheckBox);
}

/**
 * 간편 상담 신청 - 데이터 넣기
 * @param {String}  elName  금융, 푸터(모바일), 푸터(컴퓨터)
 * */
const setCommonQuickAsk = (elName) => {
    let formData = new FormData();

    const termCheckBox = document.getElementById(elName + "terms01");
    const costumerName = document.getElementById(elName + "costumerName");
    const costumerNumber = document.getElementById(elName + "costumerNumber");
    const costumerAsk = document.getElementById(elName + "costumerAsk");
    const userAgentType = userAgent;
    let entryType = "";

    if(!termCheckBox.checked){
        $popup.alert("개인정보 수집 및 동의란에 체크해 주시길 바랍니다.")
        return;
    }

    if(costumerName.value === ""){
        $popup.alert("이름 작성해 주시길 바랍니다.")
        costumerName.focus();
        return;
    }
    formData.append("costumerName", costumerName.value);

    if(costumerNumber.value === ""){
        $popup.alert("전화 번호를 작성해 주시길 바랍니다.")
        costumerNumber.focus();
        return;
    }
    formData.append("costumerNumber", costumerNumber.value);

    if(costumerAsk.value.trim() === ""){
        $popup.alert("문의 내용을 작성해 주시길 바랍니다.")
        costumerAsk.focus();
        return;
    }
    formData.append("costumerAsk", costumerAsk.value);

    if (userAgentType.indexOf("android") > -1){
        entryType = "android";
    }else if(userAgentType.indexOf("iphone") > -1){
        entryType = "iphone";
    }else if(userAgentType.indexOf("ipad") > -1){
        entryType = "ipad";
    }else if(userAgentType.indexOf("ipod") > -1){
        entryType = "ipod";
    }else{
        entryType = "web";
    }
    formData.append("entryType", entryType);

    applyCommonQuickAsk(formData);
}

/**
* 간편 상담 신청 - fetch하는 곳
* */
const applyCommonQuickAsk = (formData) => {
    fetch('/api/search/quick/inquiry/sendinfo', {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then((result) => {
            console.log(result);
            if (result.code !== 200) {
                $popup.alert(result.message);
            } else {
                $popup.close();
                $popup.alert("정상적으로 간편 상담 신청되었습니다.");
            }
        })
        .catch(error => alert(error));
}

/**
 * 간편 상담 신청란에서 일어난 이벤트인지 확인
 * @param {HTMLDocument}  element  문의 내용 요소
 * return boolean
 * */
const changeEvtInQuickInput = (element) => {

    if(element.indexOf("costumer") > -1){ // 간편 상담 신청 작성란 (text)
        return true;
    }

    if(element.indexOf("terms01") > 0){ // 간편 상담 신청 작성란 (checkbox)
        return true;
    }

    return false;
}

/**
 * 어떤 간편 상담 신청에서 일어난 건지 알아보는 이벤트
 * @param {HTMLDocument}  element     금융, 푸터(모바일), 푸터(컴퓨터)의 요소
 * @returns {String|Boolean}    금융, 푸터(모바일), 푸터(컴퓨터)의 요소 구분 값 아니면 없음
 * */
const whichQuickAskBtnEvt = (element) => {
    const btnId = element.id;

    if(btnId.indexOf("m-")> -1){
        return "m-";
    }else if(btnId.indexOf("pc-")> -1){
        return "pc-";
    }else if(btnId.indexOf("fin-")> -1){
        return "fin-";
    }
    return false;
}

/**
 * 간편 상담 신청 - 상담 신청 버튼 활성화 로직 총 집합
 * @param {HTMLDocument}  element     금융, 푸터(모바일), 푸터(컴퓨터)의 요소
 * */
const fn_activateQuickAskBtn = async (element) => {
    const isQuickElement =  await changeEvtInQuickInput(element.id);

    if(isQuickElement){
        const fstElName = await whichQuickAskBtnEvt(element);
        const isActiveBtn = await isActivateCommonQuickApplyBtn(fstElName);

        await changeQuickApplyCheckBox(element);
        if(isActiveBtn){
            const validClear = await validCommonQuickAsk(fstElName);
            await activateCommonQuickApplyBtn(fstElName, validClear);
        }
    }
}

/**
 * 간편 상담 신청 - 상담 신청 버튼 클릭 로직 총 집합
 * @param {HTMLDocument}  element       금융, 푸터(모바일), 푸터(컴퓨터)의 요소
 * @returns {Event|null}                애로사항 메시지
 * */
const fn_applyQuickAsk = async (element) => {
    const fstDivName = await whichQuickAskBtnEvt(element);
    const isValid = await validCommonQuickAsk(fstDivName);

    if(isValid){
        const validMessage = await validCommonQuickAskToMsg(fstDivName);
        return $popup.alert(validMessage);
    }

    await setCommonQuickAsk(fstDivName);
    await resetCommonQuickAsk(fstDivName);
}