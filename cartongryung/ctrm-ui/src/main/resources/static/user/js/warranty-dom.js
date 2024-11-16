/*
*   Warranty DOM 가져오기
* */
const Http = new XMLHttpRequest();
const domUrl= "https://jumpwarranty.com/jumpwarranty/jump";

Http.open("GET", domUrl);
Http.send();

Http.onreadystatechange=(e)=>{
    console.log(Http.response);
    console.log(Http.responseXML);
    if (this.readyState == 4 && this.status == 200) {
        let xmlDoc = Http.responseXML;
        let tabsDoc = xmlDoc.getElementsByClassName("cartabsOuterDiv");
        let contDoc = xmlDoc.getElementsByClassName("tabInnerContents");
        console.log(tabsDoc);
        console.log(contDoc);
    }
}