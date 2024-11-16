// 위 화살표 버튼
function moveUp() {
    moveRow(true);
}

// 아래 화살표 버튼
function moveDown() {
    moveRow(false);
}

// 행 전체를 이동시키는 함수
function moveRow(isMovingUp) {
    // 라디오 버튼 전체를 가져옴
    const radioBtns = document.querySelectorAll('input[name="id"]');
    // 라디오 버튼이 선택 되어 있는 요소가 하나 이상 있는지 확인 (true, false 반환)
    const isChecked = Array.from(radioBtns).some(button => button.checked);

    if (!isChecked) {
        alert("노출순서를 변경할 행을 선택해주세요.");
        return;
    }

    // 선택된 라디오 버튼의 가장 가까운 tr을 가져옴: 한 행 전체
    const checkedRow = document.querySelector('input[name="id"]:checked').closest('tr');
    // isMovingUp: true => 이전 tr을 가져옴, false => 이후 tr을 가져옴
    const siblingRow = isMovingUp ? checkedRow.previousElementSibling : checkedRow.nextElementSibling;
    // 선택된 라디오 버튼의 가장 가까운 tbody를 가져옴
    const tbody = checkedRow.closest('tbody');

    // 선택된 행이 위로 이동하거나 아래로 이동
    if (siblingRow) {
        tbody.insertBefore(isMovingUp ? checkedRow : siblingRow, isMovingUp ? siblingRow : checkedRow);
    } else {
        alert(isMovingUp ? "맨 첫 번째 행입니다." : "맨 마지막 행입니다.")
    }
}
