mkdir -p output

# kr environment

mkdir -p output/kr/
mkdir -p output/kr/env
mkdir -p output/kr/env/log

data_path="dataset/kr/"
output_path="output/kr/"
for REG in 0.001 # 0.0001 0.0003
do
    for LR in 0.0003 # 0.001 0.003
    do
        python train_env.py\
            --model KRUserResponse\
            --reader KRDataReader\
            --train_file ${data_path}train.csv\
            --val_file ${data_path}test.csv\
            --item_meta_file ${data_path}item_info.npy\
            --user_meta_file ${data_path}user_info.npy\
            --data_separator '@'\
            --meta_data_separator ' '\
            --loss 'bce'\
            --l2_coef ${REG}\
            --lr ${LR}\
            --epoch 1\
            --seed 19\
            --model_path ${output_path}env/kr_user_env_lr${LR}_reg${REG}.model\
            --max_seq_len 50\
            --n_worker 0\
            --feature_dim 16\
            --hidden_dims 256\
            --attn_n_head 2\
            > ${output_path}env/log/kr_user_env_lr${LR}_reg${REG}.model.log
    done
done