function layoutEvent() {

	// header gnb
	$(document).on('mouseover focusin', '.gnb>li', function () {//헤더 영역에 마우스오버하면
		$(this).children('ul.depth2').addClass('on');
		$(this).on('mouseleave', function () {
			$('ul.depth2').removeClass('on');
		});
	});

	// header gnb
	$(document).on('mouseover focusin', '.gnb>li', function () {//헤더 영역에 마우스오버하면
		const showChildrens = $(this).children();
		if(showChildrens.size() < 2){
			return;
		}
		showSubMenuArray(showChildrens);
	});
	// header gnb
	$(document).on('mouseleave', '.gnb>li', function () {//헤더 영역에 마우스오버하면
		const childrens = $(this).children();
		if(childrens.size() < 2){
			return;
		}
		hideSubMenuArray(childrens);
	});
	const showSubMenuArray = (obj) => {
		// console.log("subMenuArray");
		// console.log(obj);
		Array.prototype.forEach.call(obj,(e) => {
			const elementClassList = e.classList;
			// console.log(e);

			if(elementClassList.length > 1){
				const tagName = $(e).prop("tagName").toLowerCase();

				if(tagName == "ul"){
					$(e).addClass("on");
					$(e).stop().slideDown();
				}
			}
		});
	}
	const hideSubMenuArray = (obj) => {
		// console.log("hideSubMenuArray");
		// console.log(obj);
		Array.prototype.forEach.call(obj,(e) => {
			const elementClassList = e.classList;
			// console.log(e);

			if(elementClassList.length > 1){
				const tagName = $(e).prop("tagName").toLowerCase();

				console.log(tagName);
				if(tagName == "ul"){
					$(e).removeClass("on");
					$(e).stop().slideUp();
				}
			}
		});
	}

	// header 전체메뉴
	$(document).on('mouseover focusin', '.gnb .all-wrap', function () {
		$(this).find('.all-menu').addClass('on');
		$(this).on('mouseleave', function () {
			$('.all-menu').removeClass('on');
		});
	});
	$(document).on('mouseover focusin', '.gnb .all-menu .depth1', function () {
		$(this).addClass('on');
		$(this).siblings().removeClass('on');
		$(this).find('.depth2-list').addClass('on');
		$(this).siblings().find('.depth2-list').removeClass('on');
		$('.gnb .all-menu').on('mouseleave', function () {
			$('.gnb .all-menu .depth1').removeClass('on');
			$('.depth2-list').removeClass('on');
		});
	});

	// aside
	// Check if the device is mobile based on screen width
	function isMobileDevice() {
		return window.innerWidth <= 1023;
	}

	// Attach event listeners based on device type
	if (isMobileDevice()) {
		if (document.querySelector('.footer-nav .btn-menu')) {
			document.querySelector('.footer-nav .btn-menu').addEventListener('click', function () {
				asideControl(true);
			});
		}
	} else {
		document.querySelectorAll('.hd-top .is-menu').forEach(($el) => {
			$el.addEventListener('click', function () {
				asideControl(true);
			});
		});
	}

	document.querySelector('.aside-bg')?.addEventListener('click', function () {
		asideControl(false);
	});
	document.querySelector('.aside-close')?.addEventListener('click', function () {
		// console.log("layout.js");
		quickIconSet(44.5);
		asideControl(false);
	});

	function asideControl(type) {
		var body = document.body;

		if (type) {
			document.querySelector('.aside-bg').classList.add('on');
			document.querySelector('#aside-menu').classList.add('on');
			document.querySelector('.footer-nav a').classList.remove('on');
			document.querySelector('.footer-nav .btn-menu').classList.add('on');
			body.style.overflow = 'hidden'; // body 스크롤 잠금
		} else {
			document.querySelector('.aside-bg').classList.remove('on');
			document.querySelector('#aside-menu').classList.remove('on');
			document.querySelector('.footer-nav .btn-menu').classList.remove('on');
			body.style.overflow = ''; // body 스크롤 해제
		}
	}

	// footer
	$("#footer .cs-wrap .tit").on("click", function () {
		$(this).toggleClass('on');
		$("#footer .cs-wrap .cs-area").toggleClass('on');
	});

	// go top 버튼
	if (document.querySelector('.go-top')) {
		document.querySelector('.go-top').addEventListener('click', () => {
			window.scrollTo({ top: 0, behavior: 'smooth' });
		});
	}

	var quickMenu;
	window.addEventListener('scroll', () => {
		if (window.scrollY > 100) {
			document.querySelector('.quick-wrap')?.classList.add('show');
			clearTimeout(quickMenu);
			navT1 = setTimeout(function () { document.querySelector('.quick-wrap')?.classList.add('show2'); }, 10);
		} else {
			document.querySelector('.quick-wrap')?.classList.remove('show2');
			clearTimeout(quickMenu);
			quickMenu = setTimeout(function () { document.querySelector('.quick-wrap')?.classList.add('show'); }, 300);
		}
	});

	// 퀵 슬라이드
	const quickSlide = new Swiper('.quick-slide .swiper', {
		slidesPerView: 1,
		speed: 800,
		autoplay: {
			delay: 3000,
			disableOnInteraction: false,
		},
		navigation: {
			nextEl: '.quick-slide .swiper-navi .next',
			prevEl: '.quick-slide .swiper-navi .prev',
		},
		pagination: {
			el: '.quick-slide .swiper-count',
			type: 'fraction',
			// 갯수 표현 마크업
			renderFraction: function (currentClass, totalClass) {
				return `<span class="${currentClass}"></span>` +
					`<em>/</em>` +
					`<span class="${totalClass}"></span>`;
			}
		},
	});

	const quickContainer = document.querySelector('.quick-slide');

	if (quickContainer) {
		quickContainer.addEventListener('mouseenter', () => {
			quickSlide.autoplay.stop();
		});
	}

	if (quickContainer) {
		quickContainer.addEventListener('mouseleave', () => {
			quickSlide.autoplay.start();
		});
	}



	$(document).on('click', function (event) {
		if (!$(event.target).closest('.info-wrap').length && !$(event.target).closest('.info-box').length) {
			$('.quick-wrap .info-wrap .info-box').removeClass('on');
		}
	});

	$('.quick-wrap .info-wrap .img').on('mouseover', function () {
		var index = $(this).closest('.swiper-slide').index();
		$('.quick-wrap .info-wrap .info-box').removeClass('on').eq(index).addClass('on');
	});

	$('.quick-wrap .info-wrap').on('mouseleave', function () {
		var index = $(this).closest('.swiper-slide').index();
		$('.quick-wrap .info-wrap .info-box').removeClass('on').eq(index).removeClass('on');
	});

	// $('.quick-wrap .info-wrap .btn-close')?.on('click', function () {
	// 	$(this).closest('.info-box').removeClass('on');
	// });

	// Prevent closing info-box when clicking on it
	$('.quick-wrap .info-wrap .info-box').on('mouseover', function (event) {
		event.stopPropagation();
	});
}


let loadSocket = {
	'header': false,
	'aside': false,
	'footer': false
}

window.addEventListener('DOMContentLoaded', () => {
	const includeTag = document.querySelectorAll('.js-include');
	if (includeTag.length === 0) {
		layoutEvent();
	} else {
		includeTag.forEach((divBox, idx) => {
			fetch(divBox.dataset.include)
				.then(res => res.text())
				.then(function (data) {
					divBox.innerHTML = data;
					loadSocket[divBox.dataset.event] = true;

					if (loadSocket.header && loadSocket.aside && loadSocket.footer) {
						layoutEvent();
					}
				});
		});
	}
});

// 서브 페이지명
// Function to change the title
function changeHeaderTitle() {
	var headerTitle = document.querySelector('.hd-center');
	if (headerTitle) {
		var spanElement = headerTitle.querySelector('span');
		if (spanElement && typeof hdNm !== 'undefined') {
			spanElement.textContent = hdNm;
		}
	}
}

// Call the function to change the title when the page loads
window.onload = function () {
	changeHeaderTitle();
};

// $(document).on('mouseover focusin', '.location .has-select', function () {
// 	$(this).next('ul').addClass('on');
// 	$(this).parent('li').on('mouseleave', function () {
// 		$(this).find('ul').removeClass('on');
// 	});
// 	$(this).next('ul').find('li:last').on('focusout', function () {
// 		$(this).parent('ul').removeClass('on');
// 	});
// });

$(document).ready(function () {
	// select 커스텀
	$(document).on('click', function (e) {
		var container = $('.select-box');
		if (!container.is(e.target) && container.has(e.target).length === 0) {
			$('.select-box .select').removeClass("on").next().stop().slideUp('fast');
		}
	});

	$('.select-box .select').on('click', function () {
		$('.select-box .select').not(this).removeClass("on").next().stop().slideUp('fast');

		$(this).toggleClass("on");
		if ($(this).hasClass("on") === true) {
			$(this).next().stop().slideDown('fast');
		} else {
			$(this).next().stop().slideUp('fast');
		}
	});

	// toast alert
	$(".btn-toast").on("click", function () {
		const toast = document.getElementById('toast');
		toast.className = 'toast show';
		setTimeout(() => { closeToast(); }, 3000);
	});

	function closeToast() {
		const toast = document.getElementById('toast');
		toast.className = toast.className.replace('show', '');
	}

	/* === Find Car === */

	// 메뉴 slideup,down
	$(".schlist .schlist-tit").on("click", function () {
		var schlist = $(this).parent();
		$(schlist).toggleClass("on");
		if ($(schlist).hasClass("on") === true) {
			$(schlist).find(".schlist-cont").stop().slideDown();
		} else {
			$(schlist).find(".schlist-cont").stop().slideUp();
		}
	})

	$(".m-find .is-toggle").on("click", function () {
		$(this).toggleClass("on");
		if ($(this).hasClass("on") === true) {
			$(this).next("dd").stop().slideDown();
		} else {
			$(this).next("dd").stop().slideUp();
		}
	});

	// checkbox disabled 숫자 색
	$(".checkbox input[disabled]").parent().next().css('color', '#A8A7A7');

	// color, option 팝업
	$('.schlist .schlist-cont .btn-popup').on('click', function () {
		$(this).next().show();
	});
	$('.schlist .schlist-cont .popup-close').on('click', function () {
		$(this).parent().parent().hide();
	});

	/* === Find Car 모바일 === */
	// range slider new
	// $("#slider-range").slider({
	// 	range: true,
	// 	min: 0,
	// 	max: 2000,
	// 	values: [0, 2000],
	// 	step: 100, // 100단위로 움직이게 설정
	// 	slide: function (event, ui) {
	// 		if (ui.values[0] === 0 && ui.values[1] === 2000) {
	// 			$("#range-text").hide();
	// 			$("#full-range").show();
	// 		} else {
	// 			$("#range-text").css("display", "flex");
	// 			$("#full-range").hide();
	// 			$("#min-value").text(ui.values[0]);
	// 			$("#max-value").text(ui.values[1]);
	// 		}
	// 	}
	// });
    //
	// // 초기 설정값 검사
	// if ($("#slider-range").slider("values", 0) === 0 && $("#slider-range").slider("values", 1) === 2000) {
	// 	$("#range-text").hide();
	// 	$("#full-range").show();
	// } else {
	// 	$("#range-text").css("display", "flex");
	// 	$("#full-range").hide();
	// 	$("#min-value").text($("#slider-range").slider("values", 0));
	// 	$("#max-value").text($("#slider-range").slider("values", 1));
	// }
	
    // 팝업에서 입력된 값으로 슬라이더 값을 업데이트
    $(".popup-confirm-event").on("click", function() {
        var minPrice = parseInt($("#priceDirectPopup input[name='range']").eq(0).val());
        var maxPrice = parseInt($("#priceDirectPopup input[name='range']").eq(1).val());

        if (!isNaN(minPrice) && !isNaN(maxPrice)) {
            // 슬라이더 값 업데이트
            $("#slider-range").slider("values", [minPrice, maxPrice]);

            // 슬라이더 텍스트 업데이트
            $("#min-value").text(minPrice);
            $("#max-value").text(maxPrice);

            if (minPrice === 0 && maxPrice === 2000) {
                $("#range-text").hide();
                $("#full-range").show();
            } else {
                $("#range-text").css("display", "flex");
                $("#full-range").hide();
            }
        }
        // 팝업 닫기
        $popup.close();
    });
	// range slider
	// function initializeSlider(sliderId) {
	// 	const inputLeft = document.getElementById(`${sliderId}-left`);
	// 	const inputRight = document.getElementById(`${sliderId}-right`);
	// 	const thumbLeft = document.querySelector(`#${sliderId} .thumb.left`);
	// 	const thumbRight = document.querySelector(`#${sliderId} .thumb.right`);
	// 	const range = document.querySelector(`#${sliderId} .range`);

	// 	const setLeftValue = (e) => {
	// 		const _this = e.target;
	// 		let { value, min, max } = _this;

	// 		// 100의 배수로 변경
	// 		value = Math.floor(value / 100) * 100;

	// 		if (+inputRight.value - +value < 100) {
	// 			value = +inputRight.value - 100;
	// 		}

	// 		const percent = ((+value - +min) / (+max - +min)) * 100;

	// 		thumbLeft.style.left = `${percent}%`;
	// 		range.style.left = `${percent}%`;

	// 		// 값 표시 업데이트
	// 		document.querySelector(`#${sliderId} .value-label.left`).textContent = `${value}`;
	// 	};

	// 	const setRightValue = (e) => {
	// 		const _this = e.target;
	// 		let { value, min, max } = _this;

	// 		// 100의 배수로 변경
	// 		value = Math.ceil(value / 100) * 100;

	// 		if (+value - +inputLeft.value < 100) {
	// 			value = +inputLeft.value + 100;
	// 		}

	// 		const percent = ((+value - +min) / (+max - +min)) * 100;

	// 		thumbRight.style.right = `${100 - percent}%`;
	// 		range.style.right = `${100 - percent}%`;

	// 		// 값 표시 업데이트
	// 		document.querySelector(`#${sliderId} .value-label.right`).textContent = `${value}`;
	// 	};

	// 	if (inputLeft && inputRight) {
	// 		inputLeft.addEventListener("input", setLeftValue);
	// 		inputRight.addEventListener("input", setRightValue);
	// 	}
	// }

	// // Array of slider IDs
	// const sliderIds = ["slider1", "slider2", "slider3", "slider4", "slider5"];

	// // Function to initialize sliders
	// function initializeSliders(ids) {
	// 	ids.forEach(id => initializeSlider(id));
	// }

	// // Initialize sliders
	// initializeSliders(sliderIds);
});
