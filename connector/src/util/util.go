package util

const SEED uint32 = 131

func BkdrHash(str string)(h uint32){
	for _, c := range str {
		h = h*SEED + uint32(c)
	}
	return
}
