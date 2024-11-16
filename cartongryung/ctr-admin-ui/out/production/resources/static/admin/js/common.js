/** Datepicker 이벤트 리스너 등록 및 관련 설정 */
window.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('input.ico_date').forEach(element => {
        element.autocomplete = 'off';
        $(element).datepicker({ maxDate: new Date() });

        $(element).change(() => {
            // 시작일, 종료일이 모두 있을 경우에만 동작
            if (element.parentElement.querySelectorAll('input.ico_date').length !== 2) return;

            const sdateElement = element.parentElement.querySelector('input.ico_date:first-of-type');
            const edateElement = element.parentElement.querySelector('input.ico_date:last-of-type');

            // 시작일 선택
            if (element === sdateElement && edateElement !== null) {
                if (sdateElement.value.trim().length < 1 || edateElement.value.trim().length < 1) return;
                if (sdateElement.value > edateElement.value) edateElement.value = ''; // 시작일이 종료일보다 크면 종료일 초기화
                return;
            }

            // 종료일 선택
            if (element === edateElement && sdateElement !== null) {
                if (edateElement.value.trim().length < 1 || sdateElement.value.trim().length < 1) return;
                if (edateElement.value < sdateElement.value) sdateElement.value = ''; // 종료일이 시작일보다 작으면 시작일 초기화
            }
        });
    });
});
