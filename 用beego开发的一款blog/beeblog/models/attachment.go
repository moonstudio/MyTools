package models

import (
	_ "fmt"
	"github.com/astaxie/beego/orm"
	_ "strconv"
	"time"
)

type Attachment struct {
	Id         string
	Tid        string
	AttName    string
	UpLoadTime time.Time `orm:index`
}

func AddAttachment(fileName, tid string) error {
	o := orm.NewOrm()
	att := &Attachment{Tid: tid, AttName: fileName, UpLoadTime: time.Now()}
	var err error
	_, err = o.Insert(att)
	if err != nil {
		return err
	}
	return nil

}
