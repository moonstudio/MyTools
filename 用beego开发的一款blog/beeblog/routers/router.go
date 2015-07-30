package routers

import (
	"beeblog/controllers"
	"github.com/astaxie/beego"
	"os"
)

func init() {
	beego.Router("/", &controllers.MainController{})
	beego.Router("/login", &controllers.LoginController{})
	beego.Router("/category", &controllers.CategroyController{})
	beego.Router("/topic", &controllers.TopicController{})
	beego.Router("/comment", &controllers.CommentControllers{})
	beego.Router("/xzq", &controllers.XzqController{})
	beego.AutoRouter(&controllers.TopicController{})
	// 附件处理
	os.Mkdir("attachment", os.ModePerm)
	beego.Router("/attachment/:all", &controllers.AttachController{})

}
