package models

import (
	_ "fmt"
	"github.com/astaxie/beego/orm"
	_ "strconv"
	"time"
)

type Comment struct {
	Id         int64
	TopicId    string
	NickName   string
	Comments   string    `orm:"size(5000)"`
	CreateTime time.Time `orm:"index"`
}

func AddComment(tid, nickName, comments string) error {
	o := orm.NewOrm()
	//id, _ := strconv.ParseInt(tid, 10, 64)
	comt := &Comment{TopicId: tid, NickName: nickName, Comments: comments, CreateTime: time.Now()}
	var err error
	_, err = o.Insert(comt)
	if err != nil {
		return err
	}
	return nil
}

func GetCommentById(tid string) ([]*Comment, error) {
	o := orm.NewOrm()
	comments := make([]*Comment, 0)
	qs := o.QueryTable("comment")
	_, err := qs.Filter("topicid", tid).All(&comments)
	return comments, err
}
