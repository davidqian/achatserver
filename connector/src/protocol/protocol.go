package protocol

import (
	"encoding/json"
)

type Protocol struct {
	Length uint32 //4个字节整体包长度,包含自己
	HeaderLen uint16 //2个字节包头长度,包含自己
	//header
	Version uint16 //2字节版本
	Operate uint32 //4字节操作码
	SeqId uint32 //4字节消息号
	//header
	Body string //消息体
}

func (p *Protocol)Encode()(buff []byte, err error){
	//先用 json
	/**
	buf := new(bytes.Buffer)
	binary.Write(buf, binary.BigEndian, uint32(16 + len(p.Body)))
	binary.Write(buf, binary.BigEndian, uint16(12))
	binary.Write(buf, binary.BigEndian, p.Version)
	binary.Write(buf, binary.BigEndian, p.Operate)
	binary.Write(buf, binary.BigEndian, p.SeqId)
	buf.Write(p.Body)
	buff = buf.Bytes()
	**/
	buff, err = json.Marshal(p)
	return
}

func (p *Protocol)Decode(payload []byte)(err error){
	/**
	buff := bytes.NewBuffer(payload)
	binary.Read(buff, binary.BigEndian, &(p.Length))
	binary.Read(buff, binary.BigEndian, &(p.HeaderLen))
	binary.Read(buff, binary.BigEndian, &(p.Version))
	binary.Read(buff, binary.BigEndian, &(p.Operate))
	binary.Read(buff, binary.BigEndian, &(p.SeqId))
	len := p.Length - uint32(p.HeaderLen)
	body := bytes.NewBuffer(payload[len:])
	p.Body = body.Bytes()
	**/
	err = json.Unmarshal(payload, p)
	return
}

func MakeProtocol(operate uint32, seqId uint32, body string)(proto Protocol){
	proto = Protocol{
		Version: 1, //需要从配置里那到 version
		Operate: operate,
		SeqId: seqId,
		Body: body,
	}
	return
}
