package controllers

import (
	"beeblog/models"
	"fmt"
	"github.com/astaxie/beego"
	"path"
)

type TopicController struct {
	beego.Controller
}

func (t *TopicController) Get() {

	op := t.Input().Get("op")
	switch op {
	case "add":

		return
	case "edit":
		if !checkAccount(t.Ctx) {
			t.TplNames = "login.html"
			return
		}
		id := t.Input().Get("id")

		topic, err := models.GetTopicById(id)
		t.Data["Topic"] = topic
		t.Data["Category"], _ = models.GetAllCategories()
		t.Data["IsLogin"] = checkAccount(t.Ctx)
		t.TplNames = "topic_edit.html"
		if err != nil {
			beego.Error(err)
		}
		return
	case "info":
		id := t.Input().Get("id")

		topic, err := models.GetTopicById(id)
		t.Data["Topic"] = topic
		t.Data["Category"], _ = models.GetAllCategories()
		t.Data["Comment"], _ = models.GetCommentById(id)
		t.Data["IsLogin"] = checkAccount(t.Ctx)
		t.TplNames = "topic_info.html"
		if err != nil {
			beego.Error(err)
		}
		return
	case "del":
		if !checkAccount(t.Ctx) {
			t.TplNames = "login.html"
			return
		}
		id := t.Input().Get("id")
		if len(id) == 0 {
			break
		}
		err := models.DelTopic(id)
		if err != nil {
			beego.Error(err)
		}
		t.Redirect("/topic", 301)
		return

	default:
		t.Data["IsLogin"] = checkAccount(t.Ctx)
		t.Data["IsTopic"] = true
		t.TplNames = "topic.html"
		var err error
		t.Data["Topics"], err = models.GetAllTopic()
		if err != nil {
			beego.Error(nil)
		}
	}

}

func (t *TopicController) Add() {
	if !checkAccount(t.Ctx) {
		t.TplNames = "login.html"
		return
	}
	t.Data["IsLogin"] = checkAccount(t.Ctx)
	t.Data["IsTopic"] = true
	t.Data["Category"], _ = models.GetAllCategories()
	t.Data["Sheng"], _ = models.GetXzqById("")
	t.TplNames = "topic_add.html"
}

func (t *TopicController) Post() {
	if !checkAccount(t.Ctx) {
		t.TplNames = "login.html"
		return
	}
	op := t.Input().Get("op")
	switch op {
	case "add":
		title := t.Input().Get("title")
		content := t.Input().Get("content")
		category := t.Input().Get("category")
		_, fh, ferr := t.GetFile("attachment")
		if ferr != nil {
			beego.Error(ferr)
		}
		var attachment string
		if fh != nil {
			attachment = fh.Filename
			beego.Info(attachment)
			ferr = t.SaveToFile("attachment", path.Join("attachment", attachment))
			if ferr != nil {
				beego.Error(ferr)
			}
		}
		err := models.AddTopic(title, content, category, attachment)
		if err != nil {
			beego.Error(err)
		}
		t.Redirect("/topic", 301)
		return
	case "edit":
		return
	case "editsubmit":
		id := t.Input().Get("id")
		title := t.Input().Get("title")
		content := t.Input().Get("content")
		category := t.Input().Get("category")
		_, fh, ferr := t.GetFile("attachment")
		if ferr != nil {
			beego.Error(ferr)
		}
		var attachment string
		if fh != nil {
			attachment = fh.Filename
			fmt.Println("#############################################", attachment)
			beego.Info(attachment)
			ferr = t.SaveToFile("attachment", path.Join("attachment", attachment))
			if ferr != nil {
				beego.Error(ferr)
			}
		}
		err := models.UpdateTopic(id, title, content, category, attachment)
		if err != nil {
			beego.Error(err)
		}
		t.Redirect("/topic", 301)
		return
	default:
		t.Data["IsTopic"] = true
		t.TplNames = "topic.html"
		var err error
		t.Data["Topics"], err = models.GetAllTopic()
		if err != nil {
			beego.Error(nil)
		}
	}
}
