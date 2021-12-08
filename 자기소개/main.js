const toggleBtn = document.querySelector('.navbar_btn');
const menu = document.querySelector('.navbar_menu');
const introductionM = document.querySelector('.informationM');
const introduction = document.querySelector('.information');

toggleBtn.addEventListener('click',() => {
	menu.classList.toggle('active');
	icons.classList.toggle('active');
});


$(window).resize(function() { 
	if($(window).width() < 768) {
			if($('.informationM').css('display') == 'none'){
			  $('.informationM').show();
			  $('.information').hide();
			}else{
			  $('.informationM').hide();
			  $('.information').show();
			}
		} 
});