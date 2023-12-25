import os

def recursive_replace(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".txt"):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                content = content.replace("while(", "for (;")
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)

# 获取当前脚本所在的目录
script_directory = os.path.dirname(os.path.abspath(__file__) + '/public')

# 递归处理目录下的所有.txt文件并替换内容
recursive_replace(script_directory)