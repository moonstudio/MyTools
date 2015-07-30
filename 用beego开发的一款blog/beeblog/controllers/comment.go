package controllers

import (
	"beeblog/models"
	"fmt"
	"github.com/astaxie/beego"
)

type CommentControllers struct {
	beego.Controller
}

func (c *CommentControllers) Get() {

}
func (c *CommentControllers) Post() {
	op := c.Input().Get("op")
	fmt.Println(op)
	switch op {
	case "addComment":
		id := c.Input().Get("id")
		nickname := c.Input().Get("nickname")
		comment := c.Input().Get("comment")
		models.AddComment(id, nickname, comment)
		c.Redirect("/", 301)
		return
	default:
		fmt.Println("aadd")
		c.Redirect("/", 301)
		return
	}
}
