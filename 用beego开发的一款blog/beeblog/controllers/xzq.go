package controllers

import (
	"beeblog/models"
	"encoding/json"
	"fmt"
	"github.com/astaxie/beego"
)

type XzqController struct {
	beego.Controller
}
type XzqJson struct {
	Id    int64  `json:"Id"`
	Xzq   string `json:"Xzq"`
	Xzqmc string `json:"Xzqmc"`
	Pxzq  string `json:"Pxzq"`
	Jb    string `json:"Jb"`
	Zt    string `json:"Zt"`
}

func (x *XzqController) Get() {
	// /fmt.Println("get#############################################################################")
}
func (x *XzqController) Post() {
	xzq := x.Input().Get("xzq")
	var xzqs, _ = models.GetXzqById(xzq)

	if xzqJson, err := json.Marshal(xzqs); err == nil {
		fmt.Println("###################################", string(xzqJson))
		x.Data["xzqJson"] = string(xzqJson)
	}

	x.ServeJson()
	return

}
