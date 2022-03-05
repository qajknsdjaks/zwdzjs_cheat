var flag1 = false
var flag2 = false

function frida_Memory1(pattern,offset)
{
    console.log("头部标识:" + pattern);
    //枚举内存段的属性,返回指定内存段属性地址
    var addrArray = Process.enumerateRanges("r--");

    for (var i = 0; i < addrArray.length; i++)
    {
        var addr = addrArray[i];

        Memory.scan(addr.base, addr.size, pattern,
            {
                onMatch: function (address, size)
                {
                    console.log('搜索到 ' + pattern + " 地址是:" + address.toString());
                    flag1 = true
                    Interceptor.attach(address.add(offset) , {
                        onEnter: function(args) {
                            // - STR R1,[R0, R3]
                            // this.context.r7 = 5000

                            console.log(this.context.r7)
                            this.context.r7 = 5000
                            var p1 = this.context.r5
                            var p2 = this.context.r12
                            console.log(p1)
                            console.log(p2)
                            // console.log(ptr(p1).add(p2))
                            // console.log(((ptr(p1).add(p2)) ));
                            // console.log(Memory.readInt(ptr(p1+p2) ));

                            // console.log(Memory.readUInt(ptr(p1+p2)));
                        },
                        onLeave: function(retval) {
                            return retval
                        }
                    })



                },
                onComplete: function ()
                {
                    //console.log("搜索完毕")
                }
            }
        );
    }
}

function frida_Memory2(pattern,offset)
{
    console.log("头部标识:" + pattern);
    //枚举内存段的属性,返回指定内存段属性地址
    var addrArray = Process.enumerateRanges("r-x");

    for (var i = 0; i < addrArray.length; i++)
    {
        var addr = addrArray[i];

        Memory.scan(addr.base, addr.size, pattern,
            {
                onMatch: function (address, size)
                {
                    flag2 = true
                    console.log('搜索到 ' + pattern + " 地址是:" + address.toString());
                    Interceptor.attach(address.add(offset) , {
                        onEnter: function(args) {
                            var p1 = this.context.r4
                            console.log(p1.add(0x30))

                            console.log(Memory.readInt(ptr(p1.add(0x30)) ));
                        },
                        onLeave: function(retval) {
                            return retval
                        }
                    })
                },
                onComplete: function ()
                {
                    //console.log("搜索完毕")
                }
            }
        );
    }
}

setInterval(function(){
    if(!flag1){
        (frida_Memory1("55 0C A0 E3 60 C0 80 E2 0C 20 95 E7 02 70 67 E0 0C 70 85 E7",0x10))
    }

    if(!flag2){
        (frida_Memory2("30 C0 84 E5 2E 00 00 CA 48 20 94 E5 00 00 52 E3 E4 FF FF DA 01 30 42 E2",0x0))
    }

}, 5000)
//frida -U com.popcap.pvz -l zwdzjs.js
// adb push zwdzjs.js /sdcard/com.popcap.pvz.js