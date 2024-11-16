function PopupCenter(url, title, w, h) {
    // Fixes dual-screen position                         Most browsers      Firefox
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;
    var newWindow = window.open(url, title, 'scrollbars=no, location=no, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
}

function popup_layer(a_tag) {
    var $iframe = $("<iframe><\/iframe>");
    $iframe.attr("id", "popup_iframe");
    $iframe.attr("src", typeof a_tag == "string" ? a_tag : a_tag.href);
    $iframe.attr("allowtransparency", "true");
    $iframe.attr("scrolling", "yes");
    $("body").append($iframe);
    $("#popup_iframe").css({
        "width": "100%",
        "height": "100%"
    }).wrap("<div id='popup_iframe_wrap' style='background-color:transparent; overflow:scroll; -webkit-overflow-scrolling:touch; position:fixed; left:0; top:0; right:0; bottom:0; z-index:150'><\/div>");

    $("body").css({overflow: 'hidden', 'height': '100%'});

    $("#popup_iframe_wrap").on("DOMNodeRemoved", function () {
        console.log("DOMNodeRemoved");
        $("body", parent.document).css({'overflow': 'auto', 'height': 'auto'});
    });

    return false;
}

function popup_layer_close() {
    if (parent) {
        console.log("popup_layer_close-parent");
        $("#popup_iframe_wrap", parent.document).remove();
        this.close();
    } else {
        console.log("popup_layer_close");
        $("#popup_iframe_wrap").remove();
    }

    return false;
}

$(document).ready(
    function () {
        $("li.btn_sub > a").click(
            function () {
                var submenu = $(this).next(".sub_menu");
                if (submenu.is(":visible")) {
                    submenu.slideUp();
                } else {
                    submenu.slideDown();
                }
            });
        $("li.btn_sub > a").click(function () {
            $(this).toggleClass("skin_bg");
        });


        /*  버튼 신청완료 */
        function btn_op() {
            $('.btn_op0').hide();
            $('.btn_op1').on('click', function () {
                $(this).hide().next('.btn_op0').show();
            });

            /*  gnb 활성화 */
        }

        btn_op();


        $('#lnb .openBtn').click(function () {
            $('.content , #lnb').toggleClass('menu-off');
            if ($('.content').hasClass('menu-off')) {
                $('#lnb .openBtn').text('>');
            } else {
                $('#lnb .openBtn').text('<');
            }
        });

        /* 모바일 gnb 오른쪽 메뉴 열림 */
        $('.m_header .btn_menu').on('click', function () {
            var sidemenu = $(".m_gnb_wrap");
            if (sidemenu.is(":visible")) {
                sidemenu.hide();
                sidemenu.children(".m_gnb").animate({
                    right: '-88%'
                }, 500);
                $('html').css({'overflow': 'auto', 'height': 'auto'}); //scroll hidden 해제
            } else {
                sidemenu.fadeIn();
                sidemenu.children(".m_gnb").animate({
                    right: '0'
                }, 500);
                $('html').css({'overflow': 'hidden', 'height': '100%'});
            }
        });


        /* 모바일 gnb 오른쪽 메뉴 닫힘 */
        $(".m_gnb_wrap .menu_close").click(function () {
            $(".m_gnb_wrap").children(".m_gnb").animate({
                right: '-88%'
            }, 500).parent(".m_gnb_wrap").fadeOut();
            $('html').css({'overflow': 'auto', 'height': 'auto'}); //scroll hidden 해제


        });
        $('.m_gnb .gnb_menu li button').on('click', function () {


            if ($(this).parent().hasClass('on')) {

                $(this).parent().removeClass('on')

            } else {

                $(this).parent().addClass('on').siblings().removeClass();
            }

        });

    });

$(function() {
    $.datepicker.setDefaults({
        dateFormat: 'yy-mm-dd',
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년'
    });

    $("#datepicker3").datepicker({
        maxDate: -1
    });
});

$(function () {
    $("#datepicker1, #datepicker2, #datepicker3, #datepicker4, #datepicker5, #datepicker6, #datepicker7, #datepicker8").datepicker();
});
