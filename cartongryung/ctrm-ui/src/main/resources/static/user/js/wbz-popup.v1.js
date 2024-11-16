const $popup = {
	scrollY: 0, // 스크롤 위치
	zIndex: 10000, // z-index 조절 필요시 값 변경
	count: 0,
	activePopup: [],
	$mask: `<div class="wbz-popup-bg" id="wbz-popup-bg"></div>`,
	// $popbg: `<div class="wbz-popup-bg" id="wbz-popup-bg"></div>`,
	maskInitialize: function () {
		// 최초 생성인 경우 팝업
		if (this.count === 0) {
			this.scrollY = window.scrollY; // 최초 팝업 띄울 때 스크롤 위치 저장

			document.body.insertAdjacentHTML('beforeend', this.$mask);
			document.body.style.overflow = 'hidden';
			document.body.style.position = 'fixed';
			document.body.style.top = `-${this.scrollY}px`;
			document.body.style.width = '100%';
			document.querySelector('.wbz-popup-bg').style.zIndex = this.zIndex;
		}
		this.count++;
	},
	template_song: function(target) {
		console.log(target)
		this.backgroundSetting();
		document.querySelector(target).classList.add('on');
		document.querySelector(target).style.zIndex = this.zIndex + this.count;
		this.activePopup.push(target);
	},

	backgroundSetting: function(messege) {
		if (messege != 'close') {
			// 최초 생성인 경우 팝업
			if (this.count == 0) {
				document.body.insertAdjacentHTML('beforeend', this.$mask);
			}
			this.count += 2;
		}
		// document.querySelector('#wbz-popup-bg').style.zIndex = this.zIndex + this.count - 1;
	},
	close_song: function() {
		let $activePopup = document.querySelector(this.activePopup[this.activePopup.length - 1]);
		if ($activePopup) {
			if ($activePopup.classList.contains('is-instant')) {
				// 인스턴트 팝업인 경우
				$activePopup.remove();
			} else {
				// 템플릿 팝업의 경우
				$activePopup.classList.remove('on');
			}
			this.activePopup.pop();
			this.count -= 2;
			if (this.count < 1) {
				document.querySelector('#wbz-popup-bg').remove();
			}
		} else {
			// 오류
			console.log('오류 발생: 활성화 팝업이 없음');
			return false;
		}
		this.backgroundSetting('close');
	},
	stackControl: function(isOpen) {
		let stackLength = this.activePopup.length;
		if (isOpen) {
			if (stackLength > 1) {
				document.querySelector(this.activePopup[stackLength - 2]).classList.remove('on');
			}
		} else {
			if (stackLength != 0) {
				document.querySelector(this.activePopup[stackLength - 1]).classList.add('on');
			}
		}
	},
	alert: function (...params) {
		this.maskInitialize();

		const popupId = `wbz-popup-${this.zIndex + this.count}`;
		const options = {};

		if (params.length < 1)
			throw new Error('오류 발생: alert 내용이 없음');

		params.forEach(param => {
			if (typeof param === 'string') // 문자열로 입력한 경우 content 영역으로 간주
				options.content = param;
			else if (typeof param === 'object') // 객체로 입력한 경우 옵션 추가
				Object.assign(options, param); // options 변수에 param 값의 속성 덧붙이기
			else
				throw new Error('오류 발생: 잘못된 파라미터 요청');
		});

		// 타이틀 입력되지 않은 경우 빈값 처리
		if (!options.title) options.title = '';

		// confirm 타입인 경우
		let buttonTemplate;
		if (options.isConfirm === true) {
			options.modal = true; // confirm 타입이면 강제로 modal 지정
			buttonTemplate = `
				<button type="button" class="cr_2 popup-close">취소</button>
				<button type="button" class="cr_1 popup-confirm-event">확인</button>
			`;
		} else {
			if (options.modal === undefined) options.modal = false; // modal 속성이 지정되어있지 않은 경우에만 속성을 변경
			buttonTemplate = `
				<button type="button" class="cr_1 popup-confirm-event">확인</button>
			`;
		}

		// instant 팝업 템플릿
		let template = `
			<div class="wbz-popup-cont is-instant on type-alert" id="${popupId}" style="z-index: ${this.zIndex + this.count}">
				<div class="popup-header">
					<button type="button" class="popup-close" onclick="$popup.close();">✕</button>
				</div>
				<div class="popup-cont">
					<div class="popup-text">
						${options.content}
					</div>
					<div class="popup-button">
						${buttonTemplate}
					</div>
				</div>
			</div>
		`;
		document.body.insertAdjacentHTML('beforeend', template);
		this.activePopup.push(`#${popupId}`);
		this.stackControl(true);

		// 화면 아래에서 슬라이드 되는 팝업인 경우 애니메이션 적용
		if (document.getElementById(popupId).classList.contains('type-bottom'))
			setTimeout(() => document.getElementById(popupId).classList.add('ani'), 10);

		// 모달 이벤트 설정
		if (options.modal === false)
			document.querySelector(`.wbz-popup-bg`).addEventListener('click', () => $popup.close(true));
		else
			document.getElementById(popupId).classList.add('is-modal');

		// 버튼 이벤트 설정
		document.querySelectorAll(`#${popupId} .popup-confirm-event`).forEach($button => {
			if (typeof options.confirm === 'function')
				$button.addEventListener('click', () => options.confirm());
			else
				$button.addEventListener('click', () => this.close());
		});
		document.querySelectorAll(`#${popupId} .popup-close`).forEach($button => {
			$button.addEventListener('click', () => {
				if (typeof options.close === 'function')
					options.close();
				else
					this.close();
			});
		});
	},
	confirm: function (...params) {
		let options;
		if (typeof params[0] === 'string')
			options = {
				content: params[0],
				...params[1]
			};
		else if (typeof params[0] === 'object')
			options = { ...params[0] };
		else
			throw new Error('오류 발생: $popup.confirm 에 잘못된 파라미터가 호출됨');

		options.isConfirm = true; // confirm 타입으로 강제 지정
		this.alert(options);
	},
	template: function (target) {
		this.maskInitialize();

		document.querySelector(target).classList.add('on');
		if (document.querySelector(target).classList.contains('type-bottom')) {
			setTimeout(function () {
				document.querySelector(target).classList.add('ani');
			}, 1);
		}
		document.querySelector(target).style.zIndex = '' + (this.zIndex + this.count);
		this.activePopup.push(target);
		this.stackControl(true);

		let bg = document.querySelector('.wbz-popup-bg');
		if (this.activePopup.length == 1) {
			document.querySelector('.wbz-popup-bg').addEventListener('click', function () {
				$popup.close(true);
			});
		}
	},
    close: function (isMask) {
        // activePopup 배열이 비어 있는지 확인
        if (this.activePopup.length === 0) {
            console.warn('경고: 활성화된 팝업이 없습니다.');
            return false; // 활성화된 팝업이 없으면 함수 종료
        }

        let $activePopup = document.querySelector(this.activePopup[this.activePopup.length - 1]);

        // 만약 $activePopup가 null이라면
        if ($activePopup == null) {
            console.error('오류 발생: 활성화 팝업이 없음');
            return false; // 오류 발생 시 함수 종료
        }

        // 모달 팝업이 띄워져 있는 경우 mask 영역을 누르면 닫지 않음
        if (isMask === true && $activePopup.classList.contains('is-modal')) {
            return false; // 팝업을 닫지 않음
        }

        // 팝업 배경 제거 및 body 스타일 복원
        if (this.count === 1) {
            let $popupBg = document.querySelector('.wbz-popup-bg');
            if ($popupBg) {
                $popupBg.remove();
            }

            const body = document.body;
            body.style.removeProperty('overflow');
            body.style.removeProperty('position');
            body.style.removeProperty('top');
            body.style.removeProperty('width');
            window.scrollTo(0, this.scrollY); // 팝업 띄울 당시의 스크롤 위치로 복귀
        }

        // 애니메이션 및 팝업 닫기 처리
        if ($activePopup.classList.contains('type-bottom')) {
            setTimeout(() => $activePopup.classList.remove('ani'), 10);
        } else {
            $activePopup.classList.remove('on');
        }

        // instant 팝업인 경우 애니메이션 종료 후 element 삭제
        if ($activePopup.classList.contains('is-instant')) {
            setTimeout(() => $activePopup.remove(), 400);
        }

        // 활성화된 팝업 목록에서 현재 팝업 제거
        this.activePopup.pop();
        this.stackControl(false); // 팝업 스택 제어
        this.count--; // 팝업 카운트 감소

        return true;
    },
	closeAll: function () {
		let activeCount = this.activePopup.length;
		for (let i = 0; i < activeCount; i++) {
			this.close();
		}
		this.activePopup = [];
		this.count = 0;
	}
}

// 팝업 이벤트 리스너 등록
window.addEventListener('DOMContentLoaded', () => {
	document.querySelectorAll('[data-popup^="#"][data-popup$="-pop"]').forEach(element => {
		element.addEventListener('click', () => $popup.template(element.dataset.popup));
	});
});
