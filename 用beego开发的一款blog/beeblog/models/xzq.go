package models

import (
	_ "fmt"
	"github.com/astaxie/beego/orm"
)

type Xzq struct {
	Id    int64
	Xzq   string `orm:index`
	Xzqmc string `orm:index`
	Pxzq  string `orm:index`
	Jb    string `orm:index`
	Zt    string `orm:index`
}

func GetXzqById(pxzq string) ([]*Xzq, error) {
	o := orm.NewOrm()
	var err error
	xzq := make([]*Xzq, 0)
	if pxzq == "" {
		qs := o.QueryTable("xzq")
		_, err = qs.Filter("jb", "1").All(&xzq)
	} else {
		qs := o.QueryTable("xzq")
		_, err = qs.Filter("Pxzq", pxzq).All(&xzq)
	}
	return xzq, err
}
