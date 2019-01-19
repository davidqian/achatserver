package net

type NetServerAble interface {
	Start() error
	Stop() error
}
