package pool

type ConnectionAble interface {
	WriteMessage(data []byte)(err error)
	Close()
	GetConnectionStatus()(closed bool)
}
