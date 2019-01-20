package net

type ServerAble interface {
	Start() error
	Stop() error
}
