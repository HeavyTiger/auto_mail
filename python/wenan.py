import requests, pymysql, json, random, time, re

class download(object):
    def __init__(self) -> None:
        self.vmhost = '192.168.205.200'
        self.target = 'https://api.shadiao.app/chp'
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6,zh-TW;q=0.5',
            'Connection' : 'close'
        }
        self.proxy = self.get_proxy().get("proxy")

    def get_proxy(self):
        curproxy = requests.get("http://" + self.vmhost + ":5010/get/").json()
        print('当前的proxy为：' + curproxy.get("proxy"))
        return curproxy

    def delete_proxy(self, proxy):
        requests.get("http://" + self.vmhost + ":5010/delete/?proxy={}".format(proxy))
    
    def get_sentence(self) -> str :
        # 允许重试五次
        retry_count = 5
        while retry_count > 0:
            try:

                # 使用代理访问
                req = requests.get(url = self.target, headers = self.headers, timeout = 3, proxies={"http": "http://{}".format(self.proxy)})
                # 不使用代理访问
                # req = requests.get(url = self.target, headers = self.headers, timeout = 3)
                req.encoding = 'unicode'
                dict_json = json.loads(req.text)
                str = dict_json['data']['text']
                print(str)
                if re.match('访问太快', str) :
                    retry_count -= 1
                    self.proxy = self.get_proxy().get("proxy")
                    continue
                return str
            except Exception as e :
                print(e.args)
                retry_count -= 1
                # 删除代理池中代理
                self.delete_proxy(self.proxy)
                self.proxy = self.get_proxy().get("proxy")
        return None

    def insert_wenan(self) -> bool:
        mysql_conn = pymysql.connect(host = '127.0.0.1', port = 3306, user = 'wenan', password = '123456', db = 'wenan')
        sentence = self.get_sentence()
        if sentence == None :
            return False
        sql = "INSERT IGNORE INTO chp (chp_sentence) VALUES ('{0}')".format(sentence)
        try:
            with mysql_conn.cursor() as cursor :
                cursor.execute(sql)
            mysql_conn.commit()
            return True
        except Exception as e:
            print(e.args)
            mysql_conn.rollback()

if __name__ == "__main__" : 
    dl = download()
    for i in range(500) :
        if dl.insert_wenan() :
            # 如果返回True，继续执行
            time.sleep(random.uniform(0.8, 1.5))
        else :
            # 否则延迟10min执行
            print('接口异常，阻塞10min！')
            time.sleep(60 * 10)