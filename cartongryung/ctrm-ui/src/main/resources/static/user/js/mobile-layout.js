/**
 * 모바일 좌우로 움직이는 현상
 * */
const mobileResizePage = () => {
    let mainBodySelector = document.querySelector('body');
    mainBodySelector.style.overflowX = 'hidden';
}
/**
 * 모바일 경우 메인 배너 수정
 * */
const mobileResizeMainVisual = (mainTop) =>{
    let innerMainSwiper = mainTop.querySelectorAll('.main-content .main-visual .swiper');
    let innerMainImg = mainTop.querySelectorAll('.main-content .main-visual .img');

    changeLayoutElementCss(innerMainSwiper,"max-width","335px");

    if(innerMainImg.length > 2){
        arrayResizeElement(innerMainImg,"max-width","335px");
    }else{
        changeLayoutElementCss(innerMainImg,"max-width","335px");
    }
}
/**
 * 모바일 경우 메인 배너 수정
 * */
const mobileResizeSubVisual = (mainTop) =>{
    let subImg = mainTop.querySelectorAll('.main-cont .sub-visual a');

    if(subImg.length > 2){
        arrayResizeElement(subImg,"max-width","335px");
    }else{
        changeSizeLocationCss(subImg,"max-width","335px");
    }
}

/**
 * 모바일 경우 메인 wrap 크기 수정
 * */
const mobileResizeWrap = (mainWrap, partName) =>{
    if(mainWrap === undefined) return;

    let swiperWrap = mainWrap.querySelector('.swiper-wrap'); // slide를 위한 wrap 요소

    changeSizeLocationCss(mainWrap,"max-width","335px");

    if(!swiperWrap){ // 카통령 특가 차량
        return mobileResizeWrapCont(mainWrap, partName);
    }

    let swiperSlide = swiperWrap.querySelectorAll('.swiper .swiper-wrapper .swiper-slide'); // slide를 위한 wrap 요소
    if(swiperSlide.length > 1){

        arrayResizeElement(swiperWrap,"max-width","335px");

        if(partName !== "financial"){ // 카통령 특가 차량
            return mobileResizeWrapSlide(swiperWrap, partName);
        }
    }else{
        changeLayoutElementCss(swiperSlide[0],"max-width","335px");
    }

}

/**
 * 모바일 경우 메인 wrap-slide 수정
 * */
const mobileResizeWrapSlide = (wrapElement, partName) =>{
    let slideElement = wrapElement.querySelectorAll('.prd-box.badge .prd-thumb');

    arrayResizeElementThumb(slideElement, getPseudoVariable(partName), "width", "200px");
}

/**
 * 모바일 경우 메인 slide가 없는 wrap-cont 수정
 * */
const mobileResizeWrapCont = (wrapElement, partName) =>{
    const searchContElement = "." + partName + "-cont"
    let contElement = wrapElement.querySelector(searchContElement);

    if(contElement){
        let mainElement = contElement.querySelector(".main-area");
        let listElement = contElement.querySelector(".prd-list");

        if(mainElement){
            let boxThumb = mainElement.querySelectorAll('.prd-box.badge .prd-thumb');

            if(boxThumb){
                resizeBadgeSize(boxThumb[0].querySelector("a"), getPseudoVariable(partName,"main"), "90px 30px");
            }
        }

        if(listElement){
            let boxThumb = listElement.querySelectorAll('.prd-box.badge .prd-thumb');
            console.log(boxThumb);
            // let likeBtns = listElement.querySelectorAll('.prd-box .btn-like');
            // let likeBtnIcos = listElement.querySelectorAll('.prd-box .btn-like .like-visual');

            if(boxThumb.length > 2){
                arrayResizeElementThumb(boxThumb, getPseudoVariable(partName, "cont"), "width","120px");
                // arrayResizeElementLike(likeBtns,"24px", "90px");
                // arrayResizeElementVisual(likeBtnIcos, "background-size","12px");
            }else{
                changeLayoutElementCss(boxThumb,"width","120px");
            }
        }
    }
}

/**
 * 배열 요소인 경우 수정
 * */
const arrayResizeElement = (element, chngAttr, size) =>{
    Array.prototype.forEach.call(element,(e) => {
        changeSizeLocationCss(e,chngAttr,size);
    });
}

/**
 * thumb 배열 요소인 경우 수정
 * */
const arrayResizeElementThumb = (element, ePseudo, chngAttr, size) =>{
    Array.prototype.forEach.call(element,(e) => {
        let anchorTag = e.querySelector("a");

        changeLayoutElementCss(e,chngAttr,size);

        if(anchorTag){
            resizeBadgeSize(anchorTag, ePseudo, "72px 22px");
        }
    });
}

/**
 * 좋아요 배열 요소인 경우 수정
 * */
// const arrayResizeElementLike = (element, size, leftSize) =>{
//     Array.prototype.forEach.call(element,(e) => {
//         changeLikeBtnCss(e, size, leftSize);
//     });
// }
//
// /**
//  * 좋아요 배열 요소인 경우 수정
//  * */
// const arrayResizeElementVisual = (element, chngAttr, size) =>{
//     Array.prototype.forEach.call(element,(e) => {
//         changeLayoutElementCss(e, chngAttr, size);
//     });
// }
/**
 * thumb 배열 요소의 badge 수정
 * */
const resizeBadgeSize = (element, ePseudo, backgroundSize) =>{
    element.style.setProperty(ePseudo, backgroundSize);
}

/**
 * 요소의 size 변경 후 가운데 정렬
 * */
const changeSizeLocationCss = (element, chngAttr, size) =>{
    changeLayoutElementCss(element,chngAttr,size);
    changeLayoutElementCss(element, "margin", "0 auto");
}

/**
 * 요소의 width 및 height가 동일하게 갈 경우
 * */
const changeSameWidthHeightCss = (element, size) =>{
    changeLayoutElementCss(element,"width",size);
    changeLayoutElementCss(element,"height",size);
}

/**
 * 요소의 CSS 변경
 * */
const changeLayoutElementCss = (element, chngAttr, size) =>{
    $(element).css(chngAttr, size);
}

/**
 * like-btn 요소의 CSS 변경
 * */
const changeLikeBtnCss = (element, size, leftSize) =>{
    const cssContent = "width:" + size + ", height:" + size + ", left:" + leftSize;
    $(element).css({cssContent});
}

/**
 * 가상 요소의 변수명 가져오기
 * */
const getPseudoVariable = (partName, fstDiv) =>{
    if(partName === "recommend"){
        if(fstDiv === "main"){
            return "--recommend-main-mobile-background-size";
        }
        if(fstDiv === "cont"){
            return "--recommend-cont-mobile-background-size";
        }
    }else if(partName === "efficient"){
        return "--efficient-mobile-background-size";
    }
}



/**
 * css 어느 부분을 수정할 건지 구하는 로직
 * @param {String} element  바꿀 값
 * @param {String} measure  변경할 수단
 * @param {String} partName 영역
 * @param {String} fstDiv   모름
 * @param {String} chngAttr 어떤 속성 바꿀 건지
 * @param {String} size     대강 사이즈
 * */
// const selectCssMeasure = (element, measure, partName, fstDiv, chngAttr, size) =>{
//     if(measure === "sameWidthHeight"){
//         changeSameWidthHeightCss(element, size);
//     }else if(measure === "sameSize"){
//         changeSameWidthHeightCss(element, size);
//     }else if(measure === "sameSizeLocation"){
//         changeSizeLocationCss(element, chngAttr, size);
//     }
// }