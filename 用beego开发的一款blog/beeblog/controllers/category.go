package controllers

import (
	"beeblog/models"
	_ "fmt"
	"github.com/astaxie/beego"
	_ "github.com/astaxie/beego/context"
)

type CategroyController struct {
	beego.Controller
}

func (c *CategroyController) Get() {

	op := c.Input().Get("op")
	switch op {
	case "add":
		cname := c.Input().Get("cname")
		if len(cname) == 0 {
			break
		}
		err := models.AddCategory(cname)
		if err != nil {
			beego.Error(err)
		}
		c.Redirect("/category", 301)
		return
	case "del":
		id := c.Input().Get("id")
		if len(id) == 0 {
			break
		}
		err := models.DelCategory(id)
		if err != nil {
			beego.Error(err)
		}
		c.Redirect("/category", 301)
		return
	default:
		c.Data["IsLogin"] = checkAccount(c.Ctx)
		c.Data["IsCategray"] = true
		c.TplNames = "categroy.html"
		var err error
		c.Data["Categories"], err = models.GetAllCategories()
		if err != nil {
			beego.Error(nil)
		}
	}

}
func (c *CategroyController) Post() {
}
