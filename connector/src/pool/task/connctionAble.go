package pool

type ConnectionAble interface {
	WriteMessage(data []byte)(err error)
}
