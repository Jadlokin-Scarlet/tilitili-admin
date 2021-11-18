cur_dir=$(cd `dirname $0`; pwd)
cd $cur_dir
convert_text=$(echo $1 | $cur_dir/convert)
echo "$convert_text" | $cur_dir/speak-low > $cur_dir/voice.wav
$cur_dir/encoder $cur_dir/voice.wav $cur_dir/voice.slk -tencent
