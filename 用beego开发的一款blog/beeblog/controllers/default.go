package controllers

import (
	"beeblog/models"
	"github.com/astaxie/beego"
)

type MainController struct {
	beego.Controller
}

func (c *MainController) Get() {
	c.Data["IsHome"] = true
	c.Data["IsLogin"] = checkAccount(c.Ctx)
	var err error
	c.Data["Topics"], err = models.GetAllTopic()
	if err != nil {
		beego.Error(nil)
	}
	c.TplNames = "index.html"
}
