package pool

import "net/http"

type TaskAble interface {
	DoTask(httpClient *http.Client)(err error)
}