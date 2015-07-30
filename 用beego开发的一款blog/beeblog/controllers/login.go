package controllers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/context"
)

type LoginController struct {
	beego.Controller
}

func (l *LoginController) Get() {
	IsEdit := l.Input().Get("exit") == "true"
	if IsEdit {
		l.Data["IsLogin"] = false
		l.Ctx.SetCookie("username", "", -1)
		l.Ctx.SetCookie("password", "", -1)
		l.Redirect("/", 301)
	} else {
		l.TplNames = "login.html"
	}
	return
}

func (l *LoginController) Post() {
	username := l.Input().Get("username")
	password := l.Input().Get("password")
	if beego.AppConfig.String("username") == username && beego.AppConfig.String("password") == password {
		maxAge := 0
		maxAge = 1<<31 - 1
		l.Ctx.SetCookie("username", username, maxAge)
		l.Ctx.SetCookie("password", password, maxAge)
		l.Data["IsLogin"] = true
		l.Redirect("/", 301)
	} else {
		l.TplNames = "login.html"
	}
	return
}

//检查用户是否登录
func checkAccount(ctx *context.Context) bool {
	ck, err := ctx.Request.Cookie("username")
	if err != nil {
		return false
	}
	username := ck.Value
	ck, err = ctx.Request.Cookie("password")
	if err != nil {
		return false
	}
	password := ck.Value
	return beego.AppConfig.String("username") == username && beego.AppConfig.String("password") == password
}
