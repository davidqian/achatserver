package pool

type ConnectionAble interface {
	WriteMessage(data []byte)(error)
	Close()
	GetConnectionStatus()(bool)
	Inited()(bool)
	SetUid(string)
}
