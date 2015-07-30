package models

import (
	"fmt"
	"github.com/Unknwon/com"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/mattn/go-sqlite3"
	"os"
	"path"
	"strconv"
	"time"
)

const (
	_DB_NAME        = "data/beeblog.db"
	_SQLITE3_DRIVER = "sqlite3"
)

type Category struct {
	Id              int64
	Title           string
	CreateTime      time.Time `orm:"index"`
	Views           int64     `orm:"index"`
	TopicTime       time.Time `orm:"index"`
	TopicCount      int64
	TopicLastUserId int64
}
type Topic struct {
	Id              int64
	Uid             int64
	Title           string
	Category        string
	Content         string `orm:"size(5000)"`
	Attachment      string
	CreateTime      time.Time `orm:"index"`
	UpdateTime      time.Time `orm:"index"`
	Views           int64     `orm:"index"`
	Author          string
	ReplyTime       time.Time `orm:"index"`
	ReplyCount      int64
	ReplyLastuserId int64
}

func RegisterDB() {
	if !com.IsExist(_DB_NAME) {
		os.MkdirAll(path.Dir(_DB_NAME), os.ModePerm)
		os.Create(_DB_NAME)
	}
	orm.RegisterModel(new(Category), new(Topic), new(Comment), new(Xzq))
	orm.RegisterDriver(_SQLITE3_DRIVER, orm.DR_Sqlite)
	orm.RegisterDataBase("default", _SQLITE3_DRIVER, _DB_NAME, 10)
}

func AddCategory(name string) error {
	o := orm.NewOrm()
	cate := &Category{Title: name, CreateTime: time.Now(), TopicTime: time.Now()}
	var err error
	_, err = o.Insert(cate)
	if err != nil {
		return err
	}
	return nil

}
func GetAllCategories() ([]*Category, error) {
	o := orm.NewOrm()
	cates := make([]*Category, 0)
	qs := o.QueryTable("category")
	_, err := qs.All(&cates)
	return cates, err
}

func DelCategory(id string) error {
	cid, err := strconv.ParseInt(id, 10, 64)
	if err != nil {
		return nil
	}
	o := orm.NewOrm()
	cate := &Category{Id: cid}
	_, err = o.Delete(cate)
	return err
}
func AddTopic(title, content, category, attachment string) error {
	o := orm.NewOrm()
	topic := &Topic{Title: title, Content: content, Category: category, CreateTime: time.Now(), UpdateTime: time.Now(), ReplyTime: time.Now(), Attachment: attachment}
	var err error
	_, err = o.Insert(topic)
	if err != nil {
		return err
	}
	return nil

}

func GetTopicById(id string) (Topic, error) {
	o := orm.NewOrm()
	tid, err := strconv.ParseInt(id, 10, 64)
	topic := Topic{Id: tid}
	qs := o.Read(&topic)
	if qs != nil {
		return topic, err
	}
	return topic, nil

}
func UpdateTopic(id, title, content, category, attachment string) error {
	o := orm.NewOrm()
	tid, _ := strconv.ParseInt(id, 10, 64)
	topic := Topic{Id: tid}
	if o.Read(&topic) == nil {
		topic.Title = title
		topic.Content = content
		topic.UpdateTime = time.Now()
		topic.Category = category
		topic.Attachment = attachment
		if num, err := o.Update(&topic); err == nil {
			fmt.Println(num)
		} else {
			beego.Error(err)
		}
	}
	return nil
}

func DelTopic(id string) error {
	cid, err := strconv.ParseInt(id, 10, 64)
	if err != nil {
		return nil
	}
	o := orm.NewOrm()
	topic := &Topic{Id: cid}
	_, err = o.Delete(topic)
	return err
}
func GetAllTopic() ([]*Topic, error) {
	o := orm.NewOrm()
	topic := make([]*Topic, 0)
	qs := o.QueryTable("topic")
	_, err := qs.All(&topic)
	return topic, err
}
