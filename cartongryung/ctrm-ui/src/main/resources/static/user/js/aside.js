window.addEventListener('DOMContentLoaded', () => {
    // pc 햄버거
    const menuElement = document.querySelector('.is-menu.hide-m');
    const asideBodyElement = document.querySelector('.aside-body');
    const isLoginElement = document.querySelector('.is-login');

    if (menuElement && asideBodyElement && isLoginElement) {
        menuElement.addEventListener('click', () => {
            getMyInfo();
        });
    }

    // 모바일 더보기
    if (document.getElementById('moreButton')) {
        document.getElementById('moreButton').addEventListener('click', () => {
            // console.log("aside.js");
            quickIconSet(32.5);
            if (asideBodyElement && isLoginElement) {
                getMyInfo();
            }
        });
    }

    function getMyInfo() {
        fetch('/mypage/info')
            .then(response => response.json())
            .then(result => {
                if (result.code !== 200)
                    throw new Error(result.message);

                document.getElementById('pickCount').innerText = result.data.pickCount;
                document.getElementById('recentCount').innerText = result.data.recentCount;
                document.getElementById('inquiryCount').innerText = result.data.inquiryCount;
                document.getElementById('visitCount').innerText = result.data.visitCount;
                document.getElementById('vehicleCount').innerText = result.data.vehicleCount;

            })
            .catch(error => {
                console.error(error);
                alert(error);
            });
    }
});
