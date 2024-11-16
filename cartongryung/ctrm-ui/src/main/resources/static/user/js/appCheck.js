let userAgent = navigator.userAgent.toLowerCase();
// console.log("userAgent", userAgent)

window.addEventListener('DOMContentLoaded', () => {
    if (userAgent.indexOf('android') > -1 || userAgent.indexOf("iphone") > -1|| userAgent.indexOf("ipad") > -1|| userAgent.indexOf("ipod") > -1 ) {
        // 좌우로 흔들리는 현상 삭제
        mobileResizePage();

        // 메인 팝업
        let mainPopup = document.getElementById('mainPopup');
        if (mainPopup) {
            console.log("mainPopup")
            let mainPopupTag = mainPopup.querySelector('a');
            let mainPopupTarget = mainPopupTag.getAttribute('target');
            if (mainPopupTarget === '_blank') {
            console.log("mainPopup self")
                mainPopupTag.setAttribute('target', '_self');
            }
        }

        // 상단 띠 배너
        let topStrip = document.getElementById('header');
        if (topStrip) {
            let topStripTag = topStrip.querySelector('a');
            let topStripTarget = topStripTag.getAttribute('target');
            if (topStripTarget === '_blank') {
                topStripTag.setAttribute('target', '_self');
            }
            changeSizeLocationCss(topStrip, "max-width", "377px");
        }

        let mainSearchBox = document.querySelector('.search-box.main-box');
        if(mainSearchBox){
            changeSizeLocationCss(mainSearchBox, "max-width", "335px");
        }

        // 메인 상단 배너
        let mainTop = document.querySelector('.main-top');
        if (mainTop) {
            let innerMainTop = mainTop.querySelector('#mainTop');
            let mainTopTag = innerMainTop.querySelector('a');
            let mainTopTarget = mainTopTag.getAttribute('target');
            if (mainTopTarget === '_blank') {
                console.log("mainTop self")
                mainTopTag.setAttribute('target', '_self');
            }

            // 메인 슬라이드 배너
            mobileResizeMainVisual(mainTop);
        }

        // 메인 서브 배너
        let mainSub = document.getElementById('mainSub');
        if (mainSub) {
            let mainSubTag = mainSub.querySelector('a');
            let mainSubTarget = mainSubTag.getAttribute('target');
            if (mainSubTarget === '_blank') {
                console.log("mainSub self")
                mainSubTag.setAttribute('target', '_self');
            }

            // 메인 서브 슬라이드 배너
            mobileResizeSubVisual(mainSub);
        }

        // 로그인 배너
        let loginBanner = document.getElementById('loginBanner');
        if (loginBanner) {
            let loginBannerTag = loginBanner.querySelector('a');
            let loginBannerTarget = loginBannerTag.getAttribute('target');
            if (loginBannerTarget === '_blank') {
                console.log("loginBanner self")
                loginBannerTag.setAttribute('target', '_self');
            }
        }

        // 모든 것들 싹다
        // 실시간 인기 차량
        let efficientPart = document.getElementsByClassName('efficient-wrap');
        if(efficientPart){
            mobileResizeWrap(efficientPart[0], "efficient");
        }
        // 카통령 특가
        let recommendPart = document.getElementsByClassName('recommend-wrap');
        if(recommendPart){
            mobileResizeWrap(recommendPart[0], "recommend");
        }
        // 올라온 지 얼마 안된 차량
        let newPart = document.getElementsByClassName('new-wrap');
        if(newPart){
            mobileResizeWrap(newPart[0], "new");
        }
        // 제휴 금융사
        let financialPart = document.getElementsByClassName('financial-wrap');
        if(financialPart){
            mobileResizeWrap(financialPart[0], "financial");
        }

    }

});