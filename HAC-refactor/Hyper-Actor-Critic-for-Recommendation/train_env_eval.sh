mkdir -p output

# ml1m environment

mkdir -p output/ml1m/
mkdir -p output/ml1m/env
mkdir -p output/ml1m/env/log

data_path="dataset/ml1m/"
output_path="output/ml1m/"

for REG in 0.0003
do
    for LR in 0.001 0.003 0.0003
    do
        python train_env.py\
            --model ML1MUserResponse\
            --reader ML1MDataReader\
            --train_file ${data_path}ml1m_b_test.csv\
            --val_file ${data_path}ml1m_b_test.csv\
            --item_meta_file ${data_path}item_info.npy\
            --user_meta_file ${data_path}user_info.npy\
            --data_separator '@'\
            --meta_data_separator ' '\
            --loss 'bce'\
            --l2_coef ${REG}\
            --lr ${LR}\
            --epoch 2\
            --seed 19\
            --model_path ${output_path}env/ml1m_user_env_lr${LR}_reg${REG}_eval.model\
            --max_seq_len 50\
            --n_worker 4\
            --feature_dim 16\
            --hidden_dims 256\
            --attn_n_head 2\
            > ${output_path}env/log/ml1m_user_env_lr${LR}_reg${REG}_eval.model.log
    done
done